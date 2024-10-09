package com.example.everymoment.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.everymoment.R
import com.example.everymoment.databinding.FragmentDiaryReadBinding
import com.example.everymoment.extensions.Bookmarks

class DiaryReadFragment : Fragment() {

    private lateinit var binding: FragmentDiaryReadBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiaryReadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Bookmarks.setBookmark(binding.bookmark)

        binding.bookmark.setOnClickListener {
            Bookmarks.toggleBookmark(requireContext(), binding.bookmark)
        }

        binding.diaryEditButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, DiaryEditFragment())
                addToBackStack(null)
                commit()
            }
        }
    }
}