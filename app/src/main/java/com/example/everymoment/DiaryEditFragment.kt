package com.example.everymoment

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.everymoment.databinding.EmotionWindowBinding
import com.example.everymoment.databinding.FragmentDiaryEditBinding
import java.util.Locale.Category

class DiaryEditFragment : Fragment() {

    private lateinit var binding: FragmentDiaryEditBinding
    private lateinit var emotionPopup: PopupWindow
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private var imagesArray: BooleanArray = BooleanArray(2)
    private var categoriesArray: BooleanArray = BooleanArray(2)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiaryEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        DiaryReadFragment.setBookmark(binding.bookmark)
        galleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val imageUri: Uri? = data?.data
                addImage(imageUri)
            }
        }

        binding.bookmark.setOnClickListener {
            DiaryReadFragment.toggleBookmark(requireContext(), binding.bookmark)
        }

        binding.image1.setOnClickListener {
            openGallery()
        }

        binding.image2.setOnClickListener {
            openGallery()
        }

        binding.addCategory.setOnClickListener {
            addCatrgory()
        }

        binding.addEmotion.setOnClickListener {
            showEmotionsPopup(binding.addEmotion)
        }

        binding.emotion.setOnClickListener {
            showEmotionsPopup(binding.emotion)
        }

        binding.diaryDoneButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun showEmotionsPopup(anchorView: View) {
        val popupView = EmotionWindowBinding.inflate(layoutInflater)

        emotionPopup = PopupWindow(popupView.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        emotionPopup.isOutsideTouchable = true
        emotionPopup.isFocusable = true

        val location = IntArray(2)
        anchorView.getLocationOnScreen(location)
        val xOffset = 10.dpToPx()
        val yOffset = (-2).dpToPx()
        emotionPopup.showAtLocation(anchorView, Gravity.NO_GRAVITY, location[0] + anchorView.width + xOffset, location[1] + yOffset)

        popupView.happy.text = "${String(Character.toChars(0x1F60A))}"
        popupView.sad.text = "${String(Character.toChars(0x1F622))}"
        popupView.insensitive.text = "${String(Character.toChars(0x1F610))}"
        popupView.angry.text = "${String(Character.toChars(	0x1F620))}"
        popupView.confounded.text = "${String(Character.toChars(0x1F616))}"

        val clickListener: (String) -> Unit = { emotion ->
            binding.addEmotion.visibility = View.GONE
            binding.emotion.visibility = View.VISIBLE
            when (emotion) {
                "happy" -> binding.emotion.text = popupView.happy.text
                "sad" -> binding.emotion.text = popupView.sad.text
                "insensitive" -> binding.emotion.text = popupView.insensitive.text
                "angry" -> binding.emotion.text = popupView.angry.text
                "confounded" -> binding.emotion.text = popupView.confounded.text
            }
            emotionPopup.dismiss()
        }

        popupView.happy.setOnClickListener { clickListener("happy") }
        popupView.sad.setOnClickListener { clickListener("sad") }
        popupView.insensitive.setOnClickListener { clickListener("insensitive") }
        popupView.angry.setOnClickListener { clickListener("angry") }
        popupView.confounded.setOnClickListener { clickListener("confounded") }

        popupView.root.setOnTouchListener { _, _ ->
            emotionPopup.dismiss()
            true
        }
    }

    private fun Int.dpToPx(): Int {
        return (this * Resources.getSystem().displayMetrics.density).toInt()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
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
