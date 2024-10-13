package com.engliword.wordin.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.engliword.wordin.R
import com.engliword.wordin.databinding.FragmentNavigationMainPageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavigationMainPage : Fragment() {

    private lateinit var binding: FragmentNavigationMainPageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNavigationMainPageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Підключаємо навігацію з bottomNavigation
        childFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()?.let {
            binding.bottomNavigation.setupWithNavController(it)
        }

    }

}
