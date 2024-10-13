package com.engliword.wordin.domain.usecase

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.engliword.wordin.data.googleDrive.GoogleDriveRepository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class UploadDatabaseUseCase @Inject constructor(private val repository: GoogleDriveRepository) {

    suspend fun execute(consentLauncher: ActivityResultLauncher<Intent>, scope: CoroutineScope) {
        repository.initializeGoogleDrive(
            upload = true,
            download = false,
            scope = scope,
            consentLauncher = consentLauncher
        )
    }
}
