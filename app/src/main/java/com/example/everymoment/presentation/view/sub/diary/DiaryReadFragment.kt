package com.example.everymoment.presentation.view.sub.diary

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.everymoment.R
import com.example.everymoment.data.model.network.dto.vo.DetailDiary
import com.example.everymoment.data.model.Emotions
import com.example.everymoment.data.repository.DiaryRepository
import com.example.everymoment.databinding.FragmentDiaryReadBinding
import com.example.everymoment.extensions.Bookmark
import com.example.everymoment.presentation.viewModel.DiaryViewModel
import com.example.everymoment.presentation.viewModel.DiaryViewModelFactory
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
        getDiaryinDetail()
        setClickListeners()

    }

    private fun getDiaryinDetail() {
        lifecycleScope.launch {
            viewModel.getDiaryinDetail(diaryId) { setDiaryContent(it) }
        }
    }

    private fun setDiaryContent(diary: DetailDiary?) {
        diary?.let { it ->
            Emotions.fromString(it.emoji)?.getEmotionUnicode()?.let { emotion ->
                binding.emotion.text = emotion
                binding.emotion.visibility = View.VISIBLE
            }
            binding.location.text = it.locationName
            binding.address.text = it.address
            bookmark.setBookmark(it.bookmark)
            binding.time.text = it.createAt.substring(11, 16)
            val date = it.createAt.substring(5, 10).replace("-", "월 ")
            binding.date.text = resources.getString(R.string.formatted_date, date)

            if (it.content.isNullOrEmpty()) {
                binding.content.text = ""
            } else {
                binding.content.text = it.content
            }

            if (it.categories.isNotEmpty()) {
                if (it.categories.size == 2) {
                    binding.category2.visibility = View.VISIBLE
                    binding.category2.text =
                        resources.getString(R.string.category_text, it.categories[1].categoryName)
                }
                binding.category1.visibility = View.VISIBLE
                binding.category1.text =
                    resources.getString(R.string.category_text, it.categories[0].categoryName)
                binding.categories.visibility = View.VISIBLE
            }

            setImages()
            binding.toolBar.visibility = View.VISIBLE
            binding.scrollView.visibility = View.VISIBLE
        }
    }

    private fun setImages() {
        diaryId?.let {
            viewModel.getFiles(it)
            val imagesArray = viewModel.getFilesArray()
            if (!imagesArray.isNullOrEmpty()) {
                if (imagesArray.size == 2) {
                    binding.image2.visibility = View.VISIBLE
                    binding.image2.scaleType = ImageView.ScaleType.CENTER_CROP
                    Glide.with(requireContext()).load(imagesArray[1].imageUrl).into(binding.image2)
                }
                binding.image1.visibility = View.VISIBLE
                binding.image1.scaleType = ImageView.ScaleType.CENTER_CROP
                Glide.with(requireContext()).load(imagesArray[0].imageUrl).into(binding.image1)
                binding.images.visibility = View.VISIBLE
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
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