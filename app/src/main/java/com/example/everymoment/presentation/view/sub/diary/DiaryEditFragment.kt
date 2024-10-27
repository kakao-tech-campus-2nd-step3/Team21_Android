package com.example.everymoment.presentation.view.sub.diary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.everymoment.R
import com.example.everymoment.data.model.entity.Emotions
import com.example.everymoment.data.model.network.dto.request.postEditDiary.Category
import com.example.everymoment.data.model.network.dto.request.postEditDiary.PostEditDiaryRequest
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
import com.example.everymoment.presentation.viewModel.factory.DiaryViewModelFactory

class DiaryEditFragment : Fragment() {

    private lateinit var binding: FragmentDiaryEditBinding
    private var imagesArray: BooleanArray = BooleanArray(2)
    private var categoriesArray: BooleanArray = BooleanArray(2)
    private var categoriesIdArray: Array<Int?> = arrayOf(null, null)
    private var galleryUtil = GalleryUtil(this)
    private var toPxConverter = ToPxConverter()
    private lateinit var emotionPopupManager: EmotionPopup
    private lateinit var categoryManager: CategoryPopup
    private var delSelectedImgNum: Int = 0
    private var delSelectedCategoryNum: Int = 0
    private lateinit var bookmark: Bookmark
    private var diaryId: Int? = null

    private lateinit var delEmotionDialog: CustomDialog
    private lateinit var delCategoryDialog: CustomDialog
    private lateinit var delImageDialog: CustomDialog

    private val viewModel: DiaryViewModel by activityViewModels {
        DiaryViewModelFactory(
            DiaryRepository()
        )
    }

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

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.showNavigationBar()
    }

    private fun setDiaryContent() {
        val diary = viewModel.getDiary()
        diary?.let { it ->
            if (it.emoji != "null") {
                Emotions.fromString(it.emoji)?.getEmotionUnicode()?.let { emotion ->
                    binding.emotion.text = emotion
                    binding.emotion.visibility = View.VISIBLE
                }
                binding.addEmotion.visibility = View.GONE
            } else {
                binding.addEmotion.visibility = View.VISIBLE
            }
            binding.location.setText(it.locationName)
            binding.address.setText(it.address)
            bookmark.setBookmark(viewModel.getIsBookmarked())
            binding.time.text = it.createAt.substring(11, 16)
            val date = it.createAt.substring(5, 10).replace("-", "월 ")
            binding.date.text = resources.getString(R.string.formatted_date, date)

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
                    categoriesIdArray[1] = it.categories[1].id
                }
                binding.category1.visibility = View.VISIBLE
                binding.category1.text =
                    resources.getString(R.string.category_text, it.categories[0].categoryName)
                categoriesArray[0] = true
                categoriesIdArray[0] = it.categories[0].id
            }
        }

        setImages()
        binding.toolBar.visibility = View.VISIBLE
        binding.scrollView.visibility = View.VISIBLE
    }

    private fun setImages() {
        diaryId?.let {
            val imagesArray2 = viewModel.getFilesArray()
            if (!imagesArray2.isNullOrEmpty()) {
                if (imagesArray.size == 2) {
                    binding.image2.visibility = View.VISIBLE
                    binding.image2.scaleType = ImageView.ScaleType.CENTER_CROP
                    Glide.with(requireContext()).load(imagesArray2[1].imageUrl).into(binding.image2)
                    imagesArray[1] = true
                }
                binding.image1.visibility = View.VISIBLE
                binding.image1.scaleType = ImageView.ScaleType.CENTER_CROP
                Glide.with(requireContext()).load(imagesArray2[0].imageUrl).into(binding.image1)
                binding.images.visibility = View.VISIBLE
                binding.image2.visibility = View.VISIBLE
                imagesArray[0] = true
            }
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

        delEmotionDialog = CustomDialog(
            resources.getString(R.string.del_emotion_dialog),
            resources.getString(R.string.cancel),
            resources.getString(R.string.delete),
            onPositiveClick = {
                delEmotion()
            }
        )
    }

    private fun delEmotion() {
        binding.emotion.text = null
        binding.emotion.visibility = View.GONE
        binding.addEmotion.visibility = View.VISIBLE
    }

    private fun delSelectedCategory() {
        if (delSelectedCategoryNum == 1) {
            if (categoriesArray[1]) {
                binding.category1.text = binding.category2.text
                categoriesIdArray[0] = categoriesIdArray[1]
                categoriesIdArray[1] = null
                binding.addCategory.visibility = View.VISIBLE
                binding.category2.visibility = View.GONE
                categoriesArray[1] = false
            } else {
                binding.category1.visibility = View.GONE
                categoriesArray[0] = false
                categoriesIdArray[0] = null
            }
        } else if (delSelectedCategoryNum == 2) {
            binding.category2.visibility = View.GONE
            binding.addCategory.visibility = View.VISIBLE
            categoriesArray[1] = false
            categoriesIdArray[1] = null
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
                onCategorySelected = { categoryName, categoryId ->
                    addCategory(categoryName, categoryId)
                    if (categoriesIdArray[0] == null) {
                        categoriesIdArray[0] = categoryId
                    } else {
                        categoriesIdArray[1] = categoryId
                    }
                })
        }

        binding.category1.setOnClickListener {
            categoryManager.showCategoryPopup(
                binding.address,
                0,
                categoryYOffset,
                onCategorySelected = { categoryName, categoryId ->
                    binding.category1.text = categoryName
                    categoriesIdArray[0] = categoryId
                })
        }

        binding.category2.setOnClickListener {
            categoryManager.showCategoryPopup(
                binding.address,
                0,
                categoryYOffset,
                onCategorySelected = { categoryName, categoryId ->
                    binding.category2.text = categoryName
                    categoriesIdArray[1] = categoryId
                })
        }

        binding.category1.setOnLongClickListener {
            delCategoryDialog.show(
                requireActivity().supportFragmentManager,
                "delCategoryDialog"
            )
            delSelectedCategoryNum = 1
            true
        }

        binding.category2.setOnLongClickListener {
            delCategoryDialog.show(
                requireActivity().supportFragmentManager,
                "delCategoryDialog"
            )
            delSelectedCategoryNum = 2
            true
        }

        binding.addEmotion.setOnClickListener {
            emotionPopupManager.showEmotionsPopup(binding.addEmotion, emotionXOffset)
        }

        binding.emotion.setOnClickListener {
            emotionPopupManager.showEmotionsPopup(binding.emotion, emotionXOffset)
        }

        binding.emotion.setOnLongClickListener {
            delEmotionDialog.show(
                requireActivity().supportFragmentManager,
                "delEmotionDialog"
            )
            true
        }

        binding.diaryDoneButton.setOnClickListener {
            postEditedDiary()
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun postEditedDiary() {
        val emoji = binding.emotion.text.toString()
//        val categoryList = categoriesIdArray.map {
//            it?.let { id -> Category(id) } ?: Category(0)
//        }
        val categoryList = categoriesIdArray.mapNotNull {
            it?.let { id -> Category(id) }
        }
        val request = PostEditDiaryRequest(
            address = binding.address.text.toString().lowercase(),
            categories = categoryList,
            emoji = Emotions.fromUnicode(emoji).toString(),
            content = binding.content.text.toString(),
            locationName = binding.location.text.toString()
        )
        viewModel.patchEditedDiary(request)
    }


    private fun addCategory(category: String?, categoryId: Int?) {
        if (categoriesArray[0] == false) {
            binding.category1.visibility = View.VISIBLE
            binding.category1.text = category
            categoriesArray[0] = true
            categoriesIdArray[0] = categoryId
        } else if (categoriesArray[1] == false) {
            binding.category2.visibility = View.VISIBLE
            binding.category2.text = category
            categoriesArray[1] = true
            binding.addCategory.visibility = View.GONE
            categoriesIdArray[1] = categoryId
        }
    }
}
