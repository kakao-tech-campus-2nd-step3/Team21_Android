package com.example.everymoment.presentation.view.main.search

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.everymoment.R
import com.example.everymoment.data.model.Emotions
import com.example.everymoment.databinding.FragmentSearchFilterDialogBinding
import com.example.everymoment.presentation.adapter.CategoryAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SearchFilterDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentSearchFilterDialogBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private var checkedBookmark: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchFilterDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.isDraggable = false

        setEmoji()
        setCategories()

        binding.bookmark.setOnClickListener {
            if (!checkedBookmark) {
                binding.bookmarkIcon.setImageResource(R.drawable.baseline_bookmark_24)
                binding.bookmarkDesc.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.primary_color
                    )
                )
            } else {
                binding.bookmarkIcon.setImageResource(R.drawable.gray_border_bookmark)
                binding.bookmarkDesc.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.search_gray
                    )
                )
            }
            checkedBookmark = !checkedBookmark
        }

        binding.startDate.setOnClickListener {
            showCalendarDialog(binding.startDate) {
            if (binding.startDate.text.isNotEmpty()) {
                binding.startDate.setBackgroundResource(R.drawable.search_filter_date_background)
            }}
        }

        binding.endDate.setOnClickListener {
            showCalendarDialog(binding.endDate) {
            if (binding.endDate.text.isNotEmpty()) {
                binding.endDate.setBackgroundResource(R.drawable.search_filter_date_background)
            }}
        }

        binding.reset.setOnClickListener {
            resetSelections()
        }

        binding.apply.setOnClickListener {
            if (binding.startDate.text.isNullOrEmpty() && binding.endDate.text.isNotEmpty()) {
                makeToast(resources.getString(R.string.check_start_date))
            } else if (binding.startDate.text.isNotEmpty() && binding.endDate.text.isNullOrEmpty()) {
                makeToast(resources.getString(R.string.check_end_date))
            } else if (binding.startDate.text.isNotEmpty() && binding.endDate.text.isNotEmpty()) {
                if (!checkValidTerm()) {
                    makeToast(resources.getString(R.string.invalid_term))
                }
            } else {
                dismiss()
            }
        }

    }

    private fun makeToast(string: String) {
        Toast.makeText(
            context,
            string,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setCategories() {
        val gridLayoutManager = GridLayoutManager(requireContext(), 3)
        binding.categoryRcv.layoutManager = gridLayoutManager
        categoryAdapter = CategoryAdapter()
        binding.categoryRcv.adapter = categoryAdapter

        if (categoryAdapter.itemCount != 0) {
            binding.noCategoryText.visibility = View.GONE
            binding.categoryRcv.visibility = View.VISIBLE
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun String.toLocalDate(): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        return LocalDate.parse(this, formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkValidTerm(): Boolean {
        val startDate = binding.startDate.text.toString().toLocalDate()
        val endDate = binding.endDate.text.toString().toLocalDate()
        return !startDate.isAfter(endDate)
    }

    private fun resetSelections() {
        checkedBookmark = false
        binding.bookmarkIcon.setImageResource(R.drawable.gray_border_bookmark)
        binding.bookmarkDesc.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.search_gray
            )
        )

        binding.happy.isChecked = false
        binding.sad.isChecked = false
        binding.insensitive.isChecked = false
        binding.angry.isChecked = false
        binding.confounded.isChecked = false

        binding.startDate.text = ""
        binding.startDate.setBackgroundResource(R.drawable.search_filter_date_gray_background)
        binding.endDate.text = ""
        binding.endDate.setBackgroundResource(R.drawable.search_filter_date_gray_background)

        categoryAdapter.resetSelected()
    }

    private fun changeDateBackground(input: ()->Unit) {
        input.invoke()
    }

    private fun showCalendarDialog(textView: TextView, checkInput: ()->Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        context?.let { context ->
            DatePickerDialog(context, R.style.CustomDatePicker, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate =
                    String.format("%04d.%02d.%02d", selectedYear, selectedMonth + 1, selectedDay)
                textView.text = formattedDate
                changeDateBackground(checkInput)
            }, year, month, day).show()
        }
    }

    private fun setEmoji() {
        binding.happy.text = Emotions.HAPPY.getEmotionUnicode()
        binding.happy.textOn = Emotions.HAPPY.getEmotionUnicode()
        binding.happy.textOff = Emotions.HAPPY.getEmotionUnicode()
        binding.sad.text = Emotions.SAD.getEmotionUnicode()
        binding.sad.textOn = Emotions.SAD.getEmotionUnicode()
        binding.sad.textOff = Emotions.SAD.getEmotionUnicode()
        binding.insensitive.text = Emotions.INSENSITIVE.getEmotionUnicode()
        binding.insensitive.textOn = Emotions.INSENSITIVE.getEmotionUnicode()
        binding.insensitive.textOff = Emotions.INSENSITIVE.getEmotionUnicode()
        binding.angry.text = Emotions.ANGRY.getEmotionUnicode()
        binding.angry.textOn = Emotions.ANGRY.getEmotionUnicode()
        binding.angry.textOff = Emotions.ANGRY.getEmotionUnicode()
        binding.confounded.text = Emotions.CONFOUNDED.getEmotionUnicode()
        binding.confounded.textOn = Emotions.CONFOUNDED.getEmotionUnicode()
        binding.confounded.textOff = Emotions.CONFOUNDED.getEmotionUnicode()
    }

    companion object {
        const val TAG = "SearchFilterBottomSheet"
    }

}