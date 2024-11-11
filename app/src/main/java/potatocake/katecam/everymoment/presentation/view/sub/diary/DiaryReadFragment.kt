package potatocake.katecam.everymoment.presentation.view.sub.diary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import potatocake.katecam.everymoment.R
import potatocake.katecam.everymoment.data.model.network.dto.vo.DetailDiary
import potatocake.katecam.everymoment.data.repository.DiaryRepository
import potatocake.katecam.everymoment.databinding.FragmentDiaryReadBinding
import potatocake.katecam.everymoment.extensions.Bookmark
import potatocake.katecam.everymoment.presentation.viewModel.DiaryViewModel
import potatocake.katecam.everymoment.presentation.viewModel.factory.DiaryViewModelFactory
import kotlinx.coroutines.launch

class DiaryReadFragment : Fragment() {

    private lateinit var binding: FragmentDiaryReadBinding
    private val viewModel: DiaryViewModel by activityViewModels {
        DiaryViewModelFactory(
            DiaryRepository()
        )
    }
    private var diaryId: Int? = null
    private lateinit var bookmark: Bookmark

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiaryReadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ariuem", "${arguments?.getInt("diary_id")}")

        bookmark = Bookmark(requireContext(), binding.bookmark)
        diaryId = arguments?.getInt("diary_id")
        Log.d("diaryId", diaryId.toString())
        lifecycleScope.launch {
            getDiaryinDetail()
            getImages()
            setClickListeners()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("settle54", "onResume")
        setViewModelObserver()
    }

    private fun getDiaryinDetail() {
        Log.d("settle54", "getDetailDiary")
        lifecycleScope.launch {
            viewModel.getDiaryinDetail(diaryId)
        }
    }

    private fun getImages() {
        lifecycleScope.launch {
            viewModel.getFiles(diaryId)
        }
    }

    private fun setViewModelObserver() {
        viewModel.diary.observe(viewLifecycleOwner) { diary ->
            updateDiary(diary)
        }
        viewModel.images.observe(viewLifecycleOwner) { images ->
            updateImages(images)
        }
    }

    private fun updateDiary(diary: DetailDiary) {
        Log.d("settle54", "updateDiary")
        if (diary.emoji == null) {
            binding.emotion.visibility = View.GONE
        } else {
            potatocake.katecam.everymoment.data.model.entity.Emotions.fromString(diary.emoji)?.getEmotionUnicode()?.let { emotion ->
                binding.emotion.text = emotion
                binding.emotion.visibility = View.VISIBLE
            }
        }
        binding.location.text = diary.locationName
        binding.address.text = diary.address
        bookmark.setBookmark(diary.bookmark)
        binding.time.text = diary.createAt.substring(11, 16)
        val date = diary.createAt.substring(5, 10).replace("-", "월 ")
        binding.date.text = resources.getString(R.string.formatted_date, date)

        if (diary.content.isNullOrEmpty()) {
            binding.content.text = ""
        } else {
            binding.content.text = diary.content
        }

        if (diary.categories.isNotEmpty()) {
            if (diary.categories.size == 2) {
                binding.category2.visibility = View.VISIBLE
                binding.category2.text =
                    resources.getString(R.string.category_text, diary.categories[1].categoryName)
            }
            binding.category1.visibility = View.VISIBLE
            binding.category1.text =
                resources.getString(R.string.category_text, diary.categories[0].categoryName)
            binding.category2.visibility = View.INVISIBLE
            binding.categories.visibility = View.VISIBLE
        } else {
            binding.categories.visibility = View.GONE
        }

        binding.toolBar.visibility = View.VISIBLE
        binding.scrollView.visibility = View.VISIBLE
    }

    private fun updateImages(images: List<String>) {
        if (images.isNotEmpty()) {
            if (images.size == 2) {
                binding.image2.visibility = View.VISIBLE
                binding.image2.scaleType = ImageView.ScaleType.CENTER_CROP
                Glide.with(requireContext()).load(images[1]).into(binding.image2)
            } else {
                binding.image2.visibility = View.INVISIBLE
            }
            binding.image1.visibility = View.VISIBLE
            binding.image1.scaleType = ImageView.ScaleType.CENTER_CROP
            Glide.with(requireContext()).load(images[0]).into(binding.image1)
            binding.images.visibility = View.VISIBLE
        } else {
            binding.images.visibility = View.GONE
        }
    }

    private fun setClickListeners() {
        binding.bookmark.setOnClickListener {
            bookmark.toggleBookmark()
            viewModel.updateBookmarkStatus()
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