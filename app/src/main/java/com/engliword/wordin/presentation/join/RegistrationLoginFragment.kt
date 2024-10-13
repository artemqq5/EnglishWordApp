package com.engliword.wordin.presentation.join

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.engliword.wordin.R
import com.engliword.wordin.databinding.FragmentRegistrationLoginBinding
import com.engliword.wordin.domain.UseCaseInternetConnection
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class RegistrationLoginFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationLoginBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>

    private val internetUseCase by lazy {
        UseCaseInternetConnection(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRegistrationLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ініціалізація FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Налаштування Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        googleSignInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken)
            } catch (e: ApiException) {
                // Обробка помилки
                e.printStackTrace()
            }
        }

        // Встановлення кліка для кнопки входу
        binding.googleSignInButton.setOnClickListener {
            if (internetUseCase.isInternetAvailable()) {
                signIn()
            } else {
                Snackbar.make(
                    binding.root,
                    "Немає підключення до інтернету! Неможливо авторизуватись \uD83C\uDF10",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Успішний вхід
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // Помилка автентифікації
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // Оновлення інтерфейсу, якщо вхід успішний
            findNavController().navigate(R.id.action_registrationLoginFragment_to_navigationHome)
        } else {
            // Обробка невдалої автентифікації
            Snackbar.make(
                binding.root,
                "Помилка авторизації, спробуйте пізніше ⚠\uFE0F",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

}