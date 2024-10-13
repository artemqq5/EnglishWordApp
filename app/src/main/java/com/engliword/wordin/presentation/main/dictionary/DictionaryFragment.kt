package com.engliword.wordin.presentation.main.dictionary

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.engliword.wordin.databinding.FragmentDictionaryBinding
import com.engliword.wordin.presentation.vm.GoogleDriveViewModel
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.http.FileContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream

@AndroidEntryPoint
class DictionaryFragment : Fragment() {

    private lateinit var binding: FragmentDictionaryBinding

    private val googleDriveViewModel: GoogleDriveViewModel by viewModels()
    private lateinit var consentLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDictionaryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        consentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("GoogleDrive", "Доступ до гугл диску дозволено!")
            } else{
                Log.d("GoogleDrive", "Доступ до гугл диску відхилено")
            }
        }

        binding.upload.setOnClickListener {
            uploadDatabase()
        }

        binding.download.setOnClickListener {
            downloadDatabase()
        }
    }

    private fun uploadDatabase() {
        googleDriveViewModel.uploadDatabase(
            consentLauncher,
            onSuccess = {
                // Логіка після успішного завантаження файлу
                Log.d("GoogleDrive", "База даних завантажена успішно!")
            },
            onError = { exception ->
                // Обробка помилки
                Log.e("GoogleDrive", "Помилка при завантаженні бази даних", exception)
            }
        )
    }

    private fun downloadDatabase() {
        googleDriveViewModel.downloadDatabase(
            consentLauncher,
            onSuccess = {
                // Логіка після успішного завантаження файлу
                Log.d("GoogleDrive", "База даних завантажена успішно!")
            },
            onError = { exception ->
                // Обробка помилки
                Log.e("GoogleDrive", "Помилка при завантаженні бази даних", exception)
            }
        )
    }

}
