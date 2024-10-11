package com.example.everymoment.presentation.view.sub.diary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.everymoment.databinding.FragmentDiaryEditBinding
import com.example.everymoment.extensions.Bookmarks
import com.example.everymoment.extensions.CategoryPopup
import com.example.everymoment.extensions.EmotionPopup
import com.example.everymoment.extensions.GalleryUtil
import com.example.everymoment.extensions.ToPxConverter
import com.example.everymoment.presentation.view.main.MainActivity

class DiaryEditFragment : Fragment() {

    private lateinit var binding: FragmentDiaryEditBinding
    private var imagesArray: BooleanArray = BooleanArray(2)
    private var categoriesArray: BooleanArray = BooleanArray(2)
    private var galleryUtil = GalleryUtil(this)
    private var toPxConverter = ToPxConverter()

    private var emotionXOffset = toPxConverter.dpToPx(10)
    private var categoryYOffset = toPxConverter.dpToPx(75)

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

        val categoryManager = CategoryPopup(requireActivity(), requireContext())

        binding.bookmark.setOnClickListener {
            Bookmarks.toggleBookmark(requireContext(), binding.bookmark)
        }

        binding.image1.setOnClickListener {
            galleryUtil.openGallery(onImageSelected = {
                binding.image1.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.image1.setImageURI(it)
                imagesArray[0] = true
                binding.image2.visibility = View.VISIBLE
            })
        }

        binding.image2.setOnClickListener {
            galleryUtil.openGallery(onImageSelected = {
                binding.image2.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.image2.setImageURI(it)
                imagesArray[1] = true
            })
        }

        binding.addCategory.setOnClickListener {
            categoryManager.showCategoryPopup(
                binding.address,
                0,
                categoryYOffset,
                onCategorySelected = {
                    addCategory(it)
                })
        }

        binding.category1.setOnClickListener {
            categoryManager.showCategoryPopup(
                binding.address,
                0,
                categoryYOffset,
                onCategorySelected = {
                    binding.category1.text = it
                })
        }

        binding.category2.setOnClickListener {
            categoryManager.showCategoryPopup(
                binding.address,
                0,
                categoryYOffset,
                onCategorySelected = {
                    binding.category2.text = it
                })
        }

        binding.addEmotion.setOnClickListener {
            emotionPopupManager.showEmotionsPopup(binding.addEmotion, emotionXOffset)
        }

        binding.emotion.setOnClickListener {
            emotionPopupManager.showEmotionsPopup(binding.emotion, emotionXOffset)
        }

        binding.diaryDoneButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.showNavigationBar()
    }

    private fun addCategory(category: String?) {
        if (categoriesArray[0] == false) {
            binding.category1.visibility = View.VISIBLE
            binding.category1.text = category
            categoriesArray[0] = true
        } else if (categoriesArray[1] == false) {
            binding.category2.visibility = View.VISIBLE
            binding.category2.text = category
            categoriesArray[1] = true
            binding.addCategory.visibility = View.GONE
        }
    }
}
