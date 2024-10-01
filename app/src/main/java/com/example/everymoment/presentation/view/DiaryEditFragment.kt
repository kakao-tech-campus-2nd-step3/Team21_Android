package com.example.everymoment.presentation.view

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.everymoment.databinding.FragmentDiaryEditBinding
import com.example.everymoment.extensions.Bookmarks
import com.example.everymoment.extensions.EmotionPopup
import com.example.everymoment.extensions.GalleryUtil
import com.example.everymoment.extensions.ToPxConverter

class DiaryEditFragment : Fragment() {

    private lateinit var binding: FragmentDiaryEditBinding
    private var imagesArray: BooleanArray = BooleanArray(2)
    private var categoriesArray: BooleanArray = BooleanArray(2)
    private var xOffset = ToPxConverter().dpToPx(10)

    private lateinit var galleryUtil: GalleryUtil

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiaryEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? MainActivity)?.hideNavigationBar()
        Bookmarks.setBookmark(binding.bookmark)
        val emotionPopupManager = EmotionPopup(requireContext()) { emotion ->
            binding.emotion.text = emotion.getEmotionUnicode()
            binding.addEmotion.visibility = View.GONE
            binding.emotion.visibility = View.VISIBLE
        }

        binding.bookmark.setOnClickListener {
            Bookmarks.toggleBookmark(requireContext(), binding.bookmark)
        }

        binding.image1.setOnClickListener {
            galleryUtil.openGallery()
        }

        binding.image2.setOnClickListener {
            galleryUtil.openGallery()
        }

        binding.addCategory.setOnClickListener {
            addCatrgory()
        }

        binding.addEmotion.setOnClickListener {
            emotionPopupManager.showEmotionsPopup(binding.addEmotion, xOffset)
        }

        binding.emotion.setOnClickListener {
            emotionPopupManager.showEmotionsPopup(binding.emotion, xOffset)
        }

        binding.diaryDoneButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.showNavigationBar()
    }

    private fun addImage(imageUri: Uri?) {
        if (imagesArray[0] == false) {
            binding.image1.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.image1.setImageURI(imageUri)
            binding.image2.visibility = View.VISIBLE
            imagesArray[0] = true
        } else {
            binding.image2.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.image2.setImageURI(imageUri)
            imagesArray[1] = true
        }
    }

    private fun addCatrgory() {
        if (categoriesArray[0] == false) {
            binding.category1.visibility = View.VISIBLE
            binding.category1.text = "#학교"  // 예시
            categoriesArray[0] = true
        } else if (categoriesArray[1] == false) {
            binding.category2.visibility = View.VISIBLE
            binding.category2.text = "#공부"  // 예시
            categoriesArray[1] = true
            binding.addCategory.visibility = View.GONE
        }
    }
}
