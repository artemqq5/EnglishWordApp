package com.engliword.wordin.presentation.main.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.engliword.wordin.databinding.FragmentProfileBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private val listWords = arrayOf(
        hashMapOf(
            "word" to "book",
            "transcription" to "bʊk",
            "audio_url" to "null",
            "category" to "General",
            "translate_ru" to "Книга",
            "translate_ua" to "Книга"
        ),
        hashMapOf(
            "word" to "table",
            "transcription" to "ˈteɪ.bəl",
            "audio_url" to "null",
            "category" to "General",
            "translate_ru" to "Стол",
            "translate_ua" to "Стіл"
        ),
        hashMapOf(
            "word" to "pen",
            "transcription" to "pɛn",
            "audio_url" to "null",
            "category" to "General",
            "translate_ru" to "Ручка",
            "translate_ua" to "Ручка"
        ),
        hashMapOf(
            "word" to "chair",
            "transcription" to "ʧɛər",
            "audio_url" to "null",
            "category" to "General",
            "translate_ru" to "Стул",
            "translate_ua" to "Стілець"
        ),
        hashMapOf(
            "word" to "window",
            "transcription" to "ˈwɪn.doʊ",
            "audio_url" to "null",
            "category" to "General",
            "translate_ru" to "Окно",
            "translate_ua" to "Вікно"
        ),
        hashMapOf(
            "word" to "door",
            "transcription" to "dɔːr",
            "audio_url" to "null",
            "category" to "General",
            "translate_ru" to "Дверь",
            "translate_ua" to "Двері"
        ),
        hashMapOf(
            "word" to "water",
            "transcription" to "ˈwɔː.tər",
            "audio_url" to "null",
            "category" to "General",
            "translate_ru" to "Вода",
            "translate_ua" to "Вода"
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = Firebase.firestore

        binding.load.setOnClickListener {
            for (word in listWords) {
                db.collection("words")
                    .add(word)
                    .addOnSuccessListener {
                        Log.d("Firestore", "DocumentSnapshot added with ID: ${it.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firestore", "Error adding document", e)
                    }
            }
        }

    }
}