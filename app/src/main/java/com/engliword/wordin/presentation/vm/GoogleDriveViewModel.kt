package com.engliword.wordin.presentation.vm

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engliword.wordin.domain.usecase.DownloadDatabaseUseCase
import com.engliword.wordin.domain.usecase.UploadDatabaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoogleDriveViewModel @Inject constructor(
    private val downloadDatabaseUseCase: DownloadDatabaseUseCase,
    private val uploadDatabaseUseCase: UploadDatabaseUseCase
) : ViewModel() {

    fun uploadDatabase(consentLauncher: ActivityResultLauncher<Intent>, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                // Викликаємо use case для завантаження бази даних
                uploadDatabaseUseCase.execute(consentLauncher, viewModelScope)
                onSuccess()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun downloadDatabase(consentLauncher: ActivityResultLauncher<Intent>, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                // Викликаємо use case для завантаження бази даних
                downloadDatabaseUseCase.execute(consentLauncher, viewModelScope)
                onSuccess()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}
