package com.engliword.wordin.data.googleDrive

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.google.api.services.drive.Drive
import kotlinx.coroutines.CoroutineScope

interface GoogleDriveAPI {

    var googleDriveService: Drive

    // Initialization
    /**
     * Ініціалізує Google Drive API для завантаження або завантаження файлів.
     * @param upload Якщо true, викликається функція для завантаження файлу.
     * @param download Якщо true, викликається функція для завантаження файлу.
     * @param scope Корутина для життєвого циклу.
     * @param consentLauncher Лаунчер для запиту дозволів у користувача.
     */
    fun initializeGoogleDrive(
        upload: Boolean = false,
        download: Boolean = false,
        scope: CoroutineScope,
        consentLauncher: ActivityResultLauncher<Intent>
    )

    // Folder Operations
    /**
     * Знаходить папку за її назвою на Google Drive.
     * @param folderName Назва папки.
     * @return ID папки, якщо знайдено.
     */
    suspend fun findFolderByName(folderName: String): String?

    /**
     * Створює або знаходить папку з перевіркою дозволів.
     * @param folderName Назва папки.
     * @param consentLauncher Лаунчер для запиту дозволів у користувача.
     * @return ID папки, якщо знайдено або створено.
     */
    suspend fun checkOrCreateFolderWithPermission(
        folderName: String,
        consentLauncher: ActivityResultLauncher<Intent>
    ): String?

    /**
     * Створює папку або знаходить її, якщо вона вже існує.
     * @param folderName Назва папки.
     * @param consentLauncher Лаунчер для запиту дозволів у користувача.
     * @return ID папки.
     */
    suspend fun checkOrCreateFolder(
        folderName: String,
        consentLauncher: ActivityResultLauncher<Intent>
    ): String?

    // File Operations
    /**
     * Знаходить файл у вказаній папці на Google Drive.
     * @param folderId ID папки, де знаходиться файл.
     * @param fileName Назва файлу.
     * @return ID файлу, якщо знайдено.
     */
    suspend fun findFileInFolder(folderId: String, fileName: String): String?

    /**
     * Завантажує файл з Google Drive.
     * @param fileId ID файлу на Google Drive.
     * @param fileName Назва файлу для збереження на пристрої.
     * @param consentLauncher Лаунчер для запиту дозволів у користувача.
     */
    suspend fun downloadFileFromDrive(
        fileId: String,
        fileName: String,
        consentLauncher: ActivityResultLauncher<Intent>
    ): Any

    /**
     * Завантажує файл з Google Drive з перевіркою дозволів.
     * @param folderId ID папки, де знаходиться файл.
     * @param fileName Назва файлу.
     * @param consentLauncher Лаунчер для запиту дозволів у користувача.
     * @return ID файлу, якщо знайдено.
     */
    suspend fun checkOrDownloadFileWithPermission(
        folderId: String,
        fileName: String,
        consentLauncher: ActivityResultLauncher<Intent>
    ): String?

    // Download Operations
    /**
     * Викликає процес завантаження бази даних з Google Drive.
     * @param consentLauncher Лаунчер для запиту дозволів у користувача.
     * @param scope Корутина для життєвого циклу.
     */
    fun downloadDatabaseFromDrive(
        consentLauncher: ActivityResultLauncher<Intent>,
        scope: CoroutineScope
    )

    // Upload Operations
    /**
     * Завантажує базу даних на Google Drive.
     * @param folderId ID папки, куди завантажується файл.
     * @param databaseName Назва файлу бази даних для завантаження.
     */
    suspend fun uploadDatabaseToDrive(folderId: String, databaseName: String)

    /**
     * Створює папку і завантажує базу даних на Google Drive.
     * @param consentLauncher Лаунчер для запиту дозволів у користувача.
     * @param scope Корутина для життєвого циклу.
     */
    fun createFolderAndUploadDatabase(
        scope: CoroutineScope,
        consentLauncher: ActivityResultLauncher<Intent>
    )
}
