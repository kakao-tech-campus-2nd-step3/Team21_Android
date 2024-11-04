package com.example.everymoment.presentation.view.sub.diary

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.everymoment.R
import com.example.everymoment.data.model.entity.Emotions
import com.example.everymoment.data.model.network.dto.request.postEditDiary.Category
import com.example.everymoment.data.model.network.dto.request.postEditDiary.PatchEditedDiaryRequest
import com.example.everymoment.data.model.network.dto.vo.DetailDiary
import com.example.everymoment.data.model.network.dto.vo.File
import com.example.everymoment.data.repository.DiaryRepository
import com.example.everymoment.databinding.FragmentDiaryEditBinding
import com.example.everymoment.extensions.Bookmark
import com.example.everymoment.extensions.CategoryPopup
import com.example.everymoment.extensions.CustomDialog
import com.example.everymoment.extensions.EmotionPopup
import com.example.everymoment.extensions.GalleryUtil
import com.example.everymoment.extensions.SendFilesUtil
import com.example.everymoment.extensions.ToPxConverter
import com.example.everymoment.presentation.view.main.MainActivity
import com.example.everymoment.presentation.viewModel.DiaryViewModel
import com.example.everymoment.presentation.viewModel.factory.DiaryViewModelFactory

class DiaryEditFragment : Fragment() {

    private lateinit var binding: FragmentDiaryEditBinding

    private var imagesList: MutableList<String> = mutableListOf()
    private var categoryList: MutableList<Category> = mutableListOf()

    private var galleryUtil = GalleryUtil(this)
    private var toPxConverter = ToPxConverter()
    private var emotionXOffset = toPxConverter.dpToPx(10)
    private var categoryYOffset = toPxConverter.dpToPx(75)

    private lateinit var emotionPopupManager: EmotionPopup
    private lateinit var categoryManager: CategoryPopup

    private var delSelectedImgNum: Int = 0
    private var delSelectedCategoryNum: Int = 0
    private lateinit var bookmark: Bookmark

    private lateinit var delEmotionDialog: CustomDialog
    private lateinit var delCategoryDialog: CustomDialog
    private lateinit var delImageDialog: CustomDialog
    private lateinit var backButtonDialog: CustomDialog

    private val viewModel: DiaryViewModel by activityViewModels {
        DiaryViewModelFactory(
            DiaryRepository()
        )
    }

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
        val diary = viewModel.diary.value
        diary?.let { it ->
            if (it.emoji != null) {
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
            bookmark.setBookmark(it.bookmark)
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
                    categoryList.add(Category(it.categories[1].id))
                }
                binding.category1.visibility = View.VISIBLE
                binding.category1.text =
                    resources.getString(R.string.category_text, it.categories[0].categoryName)
                categoryList.add(Category(it.categories[0].id))
            }
        }

        setImages()
        binding.toolBar.visibility = View.VISIBLE
        binding.scrollView.visibility = View.VISIBLE
    }

    private fun setImages() {
        val filesList = viewModel.getImages()
        if (!filesList.isNullOrEmpty()) {
            binding.image1.visibility = View.VISIBLE
            binding.image1.scaleType = ImageView.ScaleType.CENTER_CROP
            Glide.with(requireContext()).load(filesList[0]).into(binding.image1)
            imagesList.add(filesList[0])
            binding.images.visibility = View.VISIBLE
            binding.image2.visibility = View.VISIBLE
            if (filesList!!.size == 2) {
                binding.image2.visibility = View.VISIBLE
                binding.image2.scaleType = ImageView.ScaleType.CENTER_CROP
                Glide.with(requireContext()).load(filesList[1]).into(binding.image2)
                imagesList.add(filesList[1])
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

        backButtonDialog = CustomDialog(
            resources.getString(R.string.back_button_dialog),
            resources.getString(R.string.cancel),
            resources.getString(R.string.do_stop),
            onPositiveClick = {
                requireActivity().supportFragmentManager.popBackStack()
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
            if (categoryList.size == 2) {
                binding.category1.text = binding.category2.text
                categoryList.removeAt(1)
                binding.addCategory.visibility = View.VISIBLE
                binding.category2.visibility = View.GONE
            } else {
                binding.category1.visibility = View.GONE
                categoryList.removeAt(0)
            }
        } else if (delSelectedCategoryNum == 2) {
            binding.category2.visibility = View.GONE
            binding.addCategory.visibility = View.VISIBLE
            categoryList.removeAt(1)
        }
        Log.d("settle54", "del category: ${categoryList.joinToString(",")}")
    }

    private fun delSelectedImage() {
        if (delSelectedImgNum == 1) {
            if (imagesList.size >= 2) {
                binding.image1.setImageDrawable(binding.image2.drawable)
                binding.image2.setImageResource(R.drawable.baseline_add_circle_outline_24)
                binding.image2.scaleType = ImageView.ScaleType.CENTER
                imagesList.removeAt(1)
            } else {
                binding.image1.setImageResource(R.drawable.baseline_add_circle_outline_24)
                binding.image1.scaleType = ImageView.ScaleType.CENTER
                binding.image2.visibility = View.INVISIBLE
                imagesList.removeAt(0)
            }
        } else if (delSelectedImgNum == 2) {
            binding.image2.setImageResource(R.drawable.baseline_add_circle_outline_24)
            binding.image2.scaleType = ImageView.ScaleType.CENTER
            imagesList.removeAt(1)
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
                binding.image2.visibility = View.VISIBLE
                if (imagesList.isEmpty()) {
                    imagesList.add(it.toString())
                } else {
                    imagesList[0] = it.toString()
                }
            })
        }

        binding.image2.setOnClickListener {
            galleryUtil.openGallery(onImageSelected = {
                binding.image2.scaleType = ImageView.ScaleType.CENTER_CROP
                Glide.with(this).load(it).into(binding.image2)
                if (imagesList.size < 2) {
                    imagesList.add(it.toString())
                } else {
                    imagesList[1] = it.toString()
                }
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
                })
        }

        binding.category1.setOnClickListener {
            categoryManager.showCategoryPopup(
                binding.address,
                0,
                categoryYOffset,
                onCategorySelected = { categoryName, categoryId ->
                    binding.category1.text = categoryName
                    categoryList[0] = Category(categoryId!!)
                    Log.d("settle54", "change category: ${categoryList.joinToString(",")}")
                })
        }

        binding.category2.setOnClickListener {
            categoryManager.showCategoryPopup(
                binding.address,
                0,
                categoryYOffset,
                onCategorySelected = { categoryName, categoryId ->
                    binding.category2.text = categoryName
                    categoryList[1] = Category(categoryId!!)
                    Log.d("settle54", "change category: ${categoryList.joinToString(",")}")
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
            patchViewModelDiary()
            patchViewModelFiles()
            patchEditedDiary { successDiary ->
                Log.d("successDiary", "$successDiary")
                if (successDiary) {
                    patchFiles {
                        Log.d("patchFiles", "$it")
                        if (it)
                            requireActivity().supportFragmentManager.popBackStack()
                    }
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            backButtonDialog.show(
                requireActivity().supportFragmentManager,
                "delEmotionDialog"
            )
        }
    }

    private fun patchViewModelFiles() {
        viewModel.patchViewModelFiles(imagesList)
    }

    private fun patchFiles(callback: (Boolean) -> Unit) {
        SendFilesUtil.uriToFile(requireContext(), imagesList) { images ->
            viewModel.patchFiles(images) { success ->
                callback(success)
            }
        }
    }

    private fun patchViewModelDiary() {
        Log.d("settle54", "${categoryList.joinToString(",")}")
        val diary = DetailDiary(
            address = binding.address.text.toString(),
            bookmark = bookmark.checkBookmarked(),
            categories = categoryList.map { category ->
                com.example.everymoment.data.model.network.dto.vo.Category(
                    viewModel.getCategoryName(category.categoryId)!!,
                    category.categoryId
                )
            },
            content = binding.content.text.toString(),
            createAt = viewModel.diary.value!!.createAt,
            emoji = Emotions.getEmotionNameInLowerCase(binding.emotion.text.toString()),
            id = viewModel.getDiaryId(),
            locationName = binding.location.text.toString()
        )
        Log.d("settle54", "view: $diary")
        viewModel.patchViewModelDiary(diary)
    }

    private fun patchEditedDiary(callback: (Boolean) -> Unit) {
        val notEmotion = notExistTextViewText(binding.emotion.text.toString())
        val notAddress = notExistTextViewText(binding.address.text.toString())
        val notLocation = notExistTextViewText(binding.location.text.toString())
        val notContent = notExistTextViewText(binding.content.text.toString())
        val notCategory = categoryList.isEmpty()

        val request = PatchEditedDiaryRequest(
            address = binding.address.text.toString(),
            addressDelete = notAddress,
            categories = categoryList,
            content = binding.content.text.toString(),
            contentDelete = notContent,
            deleteAllCategories = notCategory,
            emoji = Emotions.getEmotionNameInLowerCase(binding.emotion.text.toString()),
            emojiDelete = notEmotion,
            locationName = binding.location.text.toString(),
            locationNameDelete = notLocation
        )
        Log.d("settle54", "patch server: $request")
        viewModel.patchEditedDiary(request) { success ->
            callback(success)
        }
    }

    private fun notExistTextViewText(text: String): Boolean {
        if (text.isNullOrEmpty()) {
            return true
        } else {
            return false
        }
    }

    private fun addCategory(category: String?, categoryId: Int?) {
        if (categoryList.isEmpty()) {
            binding.category1.visibility = View.VISIBLE
            binding.category1.text = category
            categoryList.add(Category(categoryId!!))
        } else if (categoryList.size < 2) {
            binding.category2.visibility = View.VISIBLE
            binding.category2.text = category
            categoryList.add(Category(categoryId!!))
            binding.addCategory.visibility = View.GONE
        } else {
            binding.category2.text = category
            categoryList[1] = (Category(categoryId!!))
        }
        Log.d("settle54", "add category: ${categoryList.joinToString(",")}")
    }
}
