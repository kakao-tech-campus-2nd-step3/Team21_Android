package com.example.everymoment.extensions

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.example.everymoment.databinding.EmotionWindowBinding

class EmotionPopup(
    private val context: Context,
    private val onEmotionSelected: (Emotions) -> Unit
) {

    init {
        setEmotionPopup()
    }

        private lateinit var emotionPopup: PopupWindow
        private lateinit var popupView: EmotionWindowBinding


        fun showEmotionsPopup(anchorView: View, xOffset: Int = 0, yOffset: Int = 0) {
            emotionPopup = PopupWindow(
                popupView.root,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            emotionPopup.isOutsideTouchable = true
            emotionPopup.isFocusable = true

            val location = IntArray(2)
            anchorView.getLocationOnScreen(location)
            emotionPopup.showAtLocation(
                anchorView,
                Gravity.NO_GRAVITY,
                location[0] + anchorView.width + xOffset,
                location[1] + yOffset
            )

            popupView.root.setOnTouchListener { _, _ ->
                emotionPopup.dismiss()
                true
            }
        }

        private fun setEmotionPopup(): EmotionWindowBinding {
            popupView = EmotionWindowBinding.inflate((context as Activity).layoutInflater)

            popupView.happy.text = Emotions.HAPPY.getEmotionUnicode()
            popupView.sad.text = Emotions.SAD.getEmotionUnicode()
            popupView.insensitive.text = Emotions.INSENSITIVE.getEmotionUnicode()
            popupView.angry.text = Emotions.ANGRY.getEmotionUnicode()
            popupView.confounded.text = Emotions.CONFOUNDED.getEmotionUnicode()

            val clickListener: (Emotions) -> Unit = { emotion ->
                onEmotionSelected(emotion)
                emotionPopup.dismiss()
            }

            popupView.happy.setOnClickListener { clickListener(Emotions.HAPPY) }
            popupView.sad.setOnClickListener { clickListener(Emotions.SAD) }
            popupView.insensitive.setOnClickListener { clickListener(Emotions.INSENSITIVE) }
            popupView.angry.setOnClickListener { clickListener(Emotions.ANGRY) }
            popupView.confounded.setOnClickListener { clickListener(Emotions.CONFOUNDED) }

            return popupView
        }
}