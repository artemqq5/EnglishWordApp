package com.engliword.wordin.presentation.join

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.engliword.wordin.R
import com.engliword.wordin.databinding.FragmentLoadingBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoadingFragment : Fragment() {

    private lateinit var binding: FragmentLoadingBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoadingBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Виконуємо перевірку підключення та автентифікації у корутині
        lifecycleScope.launch {
            val currentUser = auth.currentUser

            delay(2000L)
            // Переходимо в головний потік для навігації
            withContext(Dispatchers.Main) {
                if (currentUser != null) {
                    // Користувач автентифікований, переходимо на головний екран
                    findNavController().navigate(R.id.action_loadingFragment_to_navigationHome)
                } else {
                    // Користувач не автентифікований, переходимо на екран входу
                    findNavController().navigate(R.id.action_loadingFragment_to_registrationLoginFragment)
                }
            }
        }
    }

}
