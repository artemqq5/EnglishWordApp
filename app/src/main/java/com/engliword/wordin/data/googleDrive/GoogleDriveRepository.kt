package com.engliword.wordin.data.googleDrive

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.http.FileContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GoogleDriveRepository(@ApplicationContext private val context: Context) : GoogleDriveAPI {

    override lateinit var googleDriveService: Drive

    override fun initializeGoogleDrive(
        upload: Boolean,
        download: Boolean,
        scope: CoroutineScope,
        consentLauncher: ActivityResultLauncher<Intent>
    ) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.email?.let { email ->
            val credential = GoogleAccountCredential.usingOAuth2(context, listOf(DriveScopes.DRIVE))
            credential.selectedAccountName = email

            googleDriveService =
                Drive.Builder(NetHttpTransport(), GsonFactory.getDefaultInstance(), credential)
                    .setApplicationName("WordIn").build()

            Log.d("GoogleDrive", "Google Drive API ініціалізовано успішно")

            when {
                upload -> createFolderAndUploadDatabase(scope, consentLauncher)
                download -> downloadDatabaseFromDrive(consentLauncher, scope)
            }
        } ?: Log.e("GoogleDrive", "Користувач не авторизований")
    }

    override suspend fun findFolderByName(folderName: String): String? =
        withContext(Dispatchers.IO) {
            try {
                val query =
                    "mimeType = 'application/vnd.google-apps.folder' and name = '$folderName'"
                googleDriveService.files().list().setQ(query).setSpaces("drive")
                    .execute().files.firstOrNull()?.id
            } catch (e: Exception) {
                Log.e("GoogleDrive", "Помилка пошуку папки", e)
                null
            }
        }

    override suspend fun checkOrCreateFolderWithPermission(
        folderName: String,
        consentLauncher: ActivityResultLauncher<Intent>
    ): String? = withContext(Dispatchers.IO) {
        try {
            findFolderByName(folderName)
        } catch (e: UserRecoverableAuthIOException) {
            withContext(Dispatchers.Main) { consentLauncher.launch(e.intent) }
            null
        } catch (e: Exception) {
            Log.e("GoogleDrive", "Помилка пошуку папки", e)
            null
        }
    }

    override suspend fun checkOrCreateFolder(
        folderName: String,
        consentLauncher: ActivityResultLauncher<Intent>
    ): String? =
        withContext(Dispatchers.IO) {
            try {
                val folderId = findFolderByName(folderName)
                folderId ?: run {
                    val fileMetadata = com.google.api.services.drive.model.File().apply {
                        name = folderName
                        mimeType = "application/vnd.google-apps.folder"
                    }
                    googleDriveService.files().create(fileMetadata).setFields("id")
                        .execute()?.id.also {
                            Log.d("GoogleDrive", "Папка створена з ID: $it")
                        }
                }
            } catch (e: UserRecoverableAuthIOException) {
                withContext(Dispatchers.Main) { consentLauncher.launch(e.intent) }
                null
            } catch (e: Exception) {
                Log.e("GoogleDrive", "Помилка створення папки на Google Drive", e)
                null
            }
        }

    override suspend fun findFileInFolder(folderId: String, fileName: String): String? =
        withContext(Dispatchers.IO) {
            try {
                val query = "'$folderId' in parents and name = '$fileName'"
                googleDriveService.files().list().setQ(query).setSpaces("drive")
                    .execute().files.firstOrNull()?.id
            } catch (e: Exception) {
                Log.e("GoogleDrive", "Помилка пошуку файлу", e)
                null
            }
        }

    override suspend fun downloadFileFromDrive(
        fileId: String,
        fileName: String,
        consentLauncher: ActivityResultLauncher<Intent>
    ) = withContext(Dispatchers.IO) {
        try {
            java.io.File(context.filesDir, fileName).apply {
                googleDriveService.files().get(fileId).executeMediaAndDownloadTo(outputStream())
            }
            Log.d("GoogleDrive", "Файл завантажено: $fileName")
        } catch (e: UserRecoverableAuthIOException) {
            withContext(Dispatchers.Main) { consentLauncher.launch(e.intent) }
        } catch (e: Exception) {
            Log.e("GoogleDrive", "Помилка завантаження файлу", e)
        }
    }

    override suspend fun checkOrDownloadFileWithPermission(
        folderId: String,
        fileName: String,
        consentLauncher: ActivityResultLauncher<Intent>
    ): String? = withContext(Dispatchers.IO) {
        try {
            findFileInFolder(folderId, fileName)
        } catch (e: UserRecoverableAuthIOException) {
            withContext(Dispatchers.Main) { consentLauncher.launch(e.intent) }
            null
        } catch (e: Exception) {
            Log.e("GoogleDrive", "Помилка пошуку файлу", e)
            null
        }
    }

    override fun downloadDatabaseFromDrive(
        consentLauncher: ActivityResultLauncher<Intent>,
        scope: CoroutineScope
    ) {
        scope.launch(Dispatchers.IO) {
            try {
                val folderId = checkOrCreateFolderWithPermission("wordInbackup", consentLauncher)
                val fileId = folderId?.let {
                    checkOrDownloadFileWithPermission(it, "wordIn.db", consentLauncher)
                }
                fileId?.let { downloadFileFromDrive(it, "wordIn.db", consentLauncher) }
            } catch (e: Exception) {
                Log.e("GoogleDrive", "Помилка завантаження файлу з Google Drive", e)
            }
        }
    }

    override suspend fun uploadDatabaseToDrive(folderId: String, databaseName: String) =
        withContext(Dispatchers.IO) {
            try {
                val tempFile = context.assets.open(databaseName).use { inputStream ->
                    val file = java.io.File(context.cacheDir, databaseName)
                    file.outputStream().use { inputStream.copyTo(it) }
                    file
                }

                val fileMetadata = com.google.api.services.drive.model.File().apply {
                    name = databaseName
                    parents = listOf(folderId)
                }

                googleDriveService.files()
                    .create(fileMetadata, FileContent("application/x-sqlite3", tempFile))
                    .setFields("id").execute()

                Log.d("GoogleDrive", "База даних завантажена успішно")
                return@withContext
            } catch (e: Exception) {
                Log.e("GoogleDrive", "Помилка завантаження бази даних на Google Drive", e)
            }
        }

    override fun createFolderAndUploadDatabase(
        scope: CoroutineScope,
        consentLauncher: ActivityResultLauncher<Intent>
    ) {
        scope.launch(Dispatchers.IO) {
            val folderId = checkOrCreateFolder("wordInbackup", consentLauncher)
            folderId?.let { uploadDatabaseToDrive(it, "wordIn.db") }
        }
    }
}
