package potatocake.katecam.everymoment.presentation.view.sub.diary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import potatocake.katecam.everymoment.R
import potatocake.katecam.everymoment.data.model.entity.Emotions
import potatocake.katecam.everymoment.data.model.network.dto.request.ManualDiaryRequest
import potatocake.katecam.everymoment.data.model.network.dto.request.postEditDiary.Category
import potatocake.katecam.everymoment.data.model.network.dto.vo.DetailDiary
import potatocake.katecam.everymoment.data.model.network.dto.vo.LocationPoint
import potatocake.katecam.everymoment.databinding.FragmentDiaryEditBinding
import potatocake.katecam.everymoment.extensions.Bookmark
import potatocake.katecam.everymoment.extensions.CategoryPopup
import potatocake.katecam.everymoment.extensions.CustomDialog
import potatocake.katecam.everymoment.extensions.EmotionPopup
import potatocake.katecam.everymoment.extensions.GalleryUtil
import potatocake.katecam.everymoment.extensions.ToPxConverter
import potatocake.katecam.everymoment.presentation.view.main.MainActivity
import potatocake.katecam.everymoment.presentation.viewModel.DiaryViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class ManualDiaryEditFragment : Fragment() {

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

    private val viewModel: DiaryViewModel by activityViewModels()

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

        val selectedDate = arguments?.getString("selected_date")

        binding.date.text = selectedDate ?: "날짜 없음"
        binding.toolBar.visibility = View.VISIBLE
        binding.scrollView.visibility = View.VISIBLE
        binding.images.visibility = View.GONE
        binding.time.visibility = View.GONE

        categoryManager = CategoryPopup(requireActivity(), requireActivity(), viewModel)

        bookmark = Bookmark(requireContext(), binding.bookmark)
        setButtonClickListeners()
        setEmotionPopup()
        setDialogs()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.showNavigationBar()
    }

    private fun setEmotionPopup() {
        emotionPopupManager = EmotionPopup(requireActivity()) { emotion ->
            binding.emotion.text = emotion.getEmotionUnicode()
            binding.addEmotion.visibility = View.GONE
            binding.emotion.visibility = View.VISIBLE
        }
    }

    private fun setDialogs() {
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

    private fun setButtonClickListeners() {
        binding.bookmark.setOnClickListener {
            bookmark.toggleBookmark()
            viewModel.updateBookmarkStatus()
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
            val selectedDateStr = arguments?.getString("formatted_date")
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val selectedDateFormatted = try {
                selectedDateStr?.let { dateFormat.format(dateFormat.parse(it)) }
            } catch (e: Exception) {
                dateFormat.format(Calendar.getInstance().time)
            }

            postManualDiary(selectedDateFormatted) { successDiary ->
                Log.d("successDiary", "$successDiary")
                if (successDiary) {
                    navigateToTodayLogFragment()
                }
            }
            //patchViewModelDiary()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            backButtonDialog.show(
                requireActivity().supportFragmentManager,
                "delEmotionDialog"
            )
        }
    }

    private fun patchViewModelDiary() {
        Log.d("settle54", "${categoryList.joinToString(",")}")
        val diary = DetailDiary(
            address = binding.address.text.toString(),
            bookmark = bookmark.checkBookmarked(),
            categories = categoryList.map { category ->
                potatocake.katecam.everymoment.data.model.network.dto.vo.Category(
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

    private fun postManualDiary(diaryDate: String?, callback: (Boolean) -> Unit) {
        val notAddress = notExistTextViewText(binding.address.text.toString())
        val notLocation = notExistTextViewText(binding.location.text.toString())
        if (notAddress == true || notLocation == true) {
            Toast.makeText(
                requireContext(),
                R.string.locantion_and_address_not_blank,
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val request = ManualDiaryRequest(
                diaryDate = diaryDate,
                locationPoint = LocationPoint(0.0, 0.0),
                address = binding.address.text.toString(),
                categories = categoryList,
                content = binding.content.text.toString(),
                emoji = Emotions.getEmotionNameInLowerCase(binding.emotion.text.toString()),
                locationName = binding.location.text.toString(),
                public = false,
                bookmark = bookmark.checkBookmarked()
            )
            Log.d("settle54", "patch server: $request")
            viewModel.postManualDiary(request) { success ->
                callback(success)
            }
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

    private fun navigateToTodayLogFragment() {
        parentFragmentManager.popBackStack()
    }
}