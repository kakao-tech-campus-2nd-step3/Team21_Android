package com.example.everymoment.presentation.view.sub.diary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.everymoment.R
import com.example.everymoment.data.dto.DetailDiary
import com.example.everymoment.data.model.Emotions
import com.example.everymoment.data.repository.DiaryRepository
import com.example.everymoment.databinding.FragmentDiaryEditBinding
import com.example.everymoment.extensions.Bookmark
import com.example.everymoment.extensions.CategoryPopup
import com.example.everymoment.extensions.CustomDialog
import com.example.everymoment.extensions.EmotionPopup
import com.example.everymoment.extensions.GalleryUtil
import com.example.everymoment.extensions.ToPxConverter
import com.example.everymoment.presentation.view.main.MainActivity
import com.example.everymoment.presentation.viewModel.DiaryViewModel
import com.example.everymoment.presentation.viewModel.DiaryViewModelFactory
import kotlinx.coroutines.launch

class DiaryEditFragment : Fragment() {

    private lateinit var binding: FragmentDiaryEditBinding
    private var imagesArray: BooleanArray = BooleanArray(2)
    private var categoriesArray: BooleanArray = BooleanArray(2)
    private var galleryUtil = GalleryUtil(this)
    private var toPxConverter = ToPxConverter()
    private lateinit var emotionPopupManager: EmotionPopup
    private lateinit var categoryManager: CategoryPopup
    private lateinit var delCategoryDialog: CustomDialog
    private lateinit var delImageDialog: CustomDialog
    private var delSelectedImgNum: Int = 0
    private var delSelectedCategoryNum: Int = 0
    private lateinit var bookmark: Bookmark
    private var diaryId: Int? = null

    private val viewModel: DiaryViewModel by activityViewModels { DiaryViewModelFactory(DiaryRepository()) }

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

        categoryManager = CategoryPopup(requireActivity(), requireContext(), viewModel)

        diaryId = viewModel.getDiaryId()
        bookmark = Bookmark(requireContext(), binding.bookmark)
        setButtonClickListeners()
        setEmotionPopup()
        setDialogs()
        setDiaryContent()

    }

    private fun setDiaryContent() {
        val diary = viewModel.getDiary()
        diary?.let { it ->
            Emotions.fromString(it.emoji)?.getEmotionUnicode()?.let { emotion ->
                binding.emotion.text = emotion
                binding.emotion.visibility = View.VISIBLE
            }
            binding.location.setText(it.locationName)
            binding.address.setText(it.address)
            bookmark.setBookmark(viewModel.getIsBookmarked())
            binding.time.text = it.createAt.substring(11, 16)
            val date = "8월 21일"
            binding.date.text = date

            if (it.content.isNullOrEmpty()) {
                binding.content.setText("")
            } else {
                binding.content.setText(it.content)
            }

            if (it.categories.isNotEmpty()) {
                if (it.categories.size == 2) {
                    binding.category2.visibility = View.VISIBLE
                    binding.category2.text =
                        resources.getString(R.string.category_text, it.categories[1].categoryName)
                    binding.addCategory.visibility = View.INVISIBLE
                    categoriesArray[1] = true
                }
                binding.category1.visibility = View.VISIBLE
                binding.category1.text =
                    resources.getString(R.string.category_text, it.categories[0].categoryName)
                binding.categories.visibility = View.VISIBLE
                categoriesArray[0] = true
            }

            if (it.file.isNotEmpty()) {
                if (it.file.size == 2) {
                    binding.image2.visibility = View.VISIBLE
                    binding.image2.scaleType = ImageView.ScaleType.CENTER_CROP
                    Glide.with(requireContext()).load(it.file[1].imageUrl).into(binding.image2)
                    imagesArray[1] = true
                }
                binding.image1.visibility = View.VISIBLE
                binding.image1.scaleType = ImageView.ScaleType.CENTER_CROP
                Glide.with(requireContext()).load(it.file[0].imageUrl).into(binding.image1)
                binding.images.visibility = View.VISIBLE
                binding.image2.visibility = View.VISIBLE
                imagesArray[0] = true
            }
            binding.toolBar.visibility = View.VISIBLE
            binding.scrollView.visibility = View.VISIBLE
        }
    }

    private fun setEmotionPopup() {
        emotionPopupManager = EmotionPopup(requireContext()) { emotion ->
            binding.emotion.text = emotion.getEmotionUnicode()
            binding.addEmotion.visibility = View.GONE
            binding.emotion.visibility = View.VISIBLE
        }
    }

    private fun setDialogs() {
        delImageDialog = CustomDialog(
            resources.getString(R.string.del_image_dialog),
            resources.getString(R.string.cancel),
            resources.getString(R.string.delete),
            onPositiveClick = {
                delSelectedImage()
            }
        )
        delCategoryDialog = CustomDialog(
            resources.getString(R.string.del_category_dialog),
            resources.getString(R.string.cancel),
            resources.getString(R.string.delete),
            onPositiveClick = {
                delSelectedCategory()
            }
        )
    }

    private fun delSelectedCategory() {
        if (delSelectedCategoryNum == 1) {
            if (categoriesArray[1]) {
                binding.category1.text = binding.category2.text
                binding.addCategory.visibility = View.VISIBLE
                binding.category2.visibility = View.GONE
                categoriesArray[1] = false
            } else {
                binding.category1.visibility = View.GONE
                categoriesArray[0] = false
            }
        } else if (delSelectedCategoryNum == 2) {
            binding.category2.visibility = View.GONE
            binding.addCategory.visibility = View.VISIBLE
            categoriesArray[1] = false
        }
    }

    private fun delSelectedImage() {
        if (delSelectedImgNum == 1) {
            if (imagesArray[1]) {
                binding.image1.setImageDrawable(binding.image2.drawable)
                binding.image2.setImageResource(R.drawable.baseline_add_circle_outline_24)
                binding.image2.scaleType = ImageView.ScaleType.CENTER
                imagesArray[1] = false
            } else {
                binding.image1.setImageResource(R.drawable.baseline_add_circle_outline_24)
                binding.image1.scaleType = ImageView.ScaleType.CENTER
                binding.image2.visibility = View.INVISIBLE
                imagesArray[0] = false
            }
        } else if (delSelectedImgNum == 2) {
            binding.image2.setImageResource(R.drawable.baseline_add_circle_outline_24)
            binding.image2.scaleType = ImageView.ScaleType.CENTER
            imagesArray[1] = false
        }
    }

    private fun setButtonClickListeners() {
        binding.bookmark.setOnClickListener {
            bookmark.toggleBookmark()
            viewModel.updateBookmarkStatus()
        }

        binding.image1.setOnClickListener {
            galleryUtil.openGallery(onImageSelected = {
                binding.image1.scaleType = ImageView.ScaleType.CENTER_CROP
                Glide.with(this).load(it).into(binding.image1)
                imagesArray[0] = true
                binding.image2.visibility = View.VISIBLE
            })
        }

        binding.image2.setOnClickListener {
            galleryUtil.openGallery(onImageSelected = {
                binding.image2.scaleType = ImageView.ScaleType.CENTER_CROP
                Glide.with(this).load(it).into(binding.image2)
                imagesArray[1] = true
            })
        }

        binding.image1.setOnLongClickListener {
            delImageDialog.show(requireActivity().supportFragmentManager, "delImageDialog")
            delSelectedImgNum = 1
            true
        }

        binding.image2.setOnLongClickListener {
            delImageDialog.show(requireActivity().supportFragmentManager, "delImageDialog")
            delSelectedImgNum = 2
            true
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

        binding.category1.setOnLongClickListener {
            delCategoryDialog.show(requireActivity().supportFragmentManager, "delCategoryDialog")
            delSelectedCategoryNum = 1
            true
        }

        binding.category2.setOnLongClickListener {
            delCategoryDialog.show(requireActivity().supportFragmentManager, "delCategoryDialog")
            delSelectedCategoryNum = 2
            true
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
