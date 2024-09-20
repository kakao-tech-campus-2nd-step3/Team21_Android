package com.example.everymoment.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.example.everymoment.R
import com.example.everymoment.databinding.FragmentDiaryReadBinding

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

        setBookmark(binding.bookmark)

        binding.bookmark.setOnClickListener {
            toggleBookmark(requireContext(), binding.bookmark)
        }

        binding.diaryEditButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, DiaryEditFragment())
                addToBackStack(null)
                commit()
            }
        }
    }

    companion object Bookmark {
        var isBookmarked: Boolean = false

        fun setBookmark(bookmarkId: ImageView) {
            if (isBookmarked == false) {
                bookmarkId.setImageResource(R.drawable.baseline_bookmark_border_24)
            } else {
                bookmarkId.setImageResource(R.drawable.baseline_bookmark_24)
            }
        }

        fun toggleBookmark(context: Context, bookmarkId: ImageView) {
            Log.d("bookmark", "bookmark clicked")
            if (isBookmarked == false) {
                bookmarkId.setImageResource(R.drawable.baseline_bookmark_24)
                Toast.makeText(
                    context,
                    context.getString(R.string.add_bookmark),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else {
                bookmarkId.setImageResource(R.drawable.baseline_bookmark_border_24)
                Toast.makeText(
                    context,
                    context.getString(R.string.remove_bookmark),
                    Toast.LENGTH_SHORT
                ).show()
            }
            isBookmarked = !isBookmarked
        }
    }
}