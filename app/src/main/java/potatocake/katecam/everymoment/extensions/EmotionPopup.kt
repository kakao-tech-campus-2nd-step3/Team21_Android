package potatocake.katecam.everymoment.extensions

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import potatocake.katecam.everymoment.databinding.EmotionWindowBinding

class EmotionPopup(
    private val context: Context,
    private val onEmotionSelected: (potatocake.katecam.everymoment.data.model.entity.Emotions) -> Unit
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
            val inflater = when (context) {
                is Activity -> {
                    (context as Activity).layoutInflater
                }
                is Fragment -> {
                    (context as Fragment).requireActivity().layoutInflater
                }
                else -> {
                    throw IllegalArgumentException("Context is not valid")
                }
            }

            popupView = EmotionWindowBinding.inflate(inflater)

            popupView.happy.text = potatocake.katecam.everymoment.data.model.entity.Emotions.HAPPY.getEmotionUnicode()
            popupView.sad.text = potatocake.katecam.everymoment.data.model.entity.Emotions.SAD.getEmotionUnicode()
            popupView.insensitive.text = potatocake.katecam.everymoment.data.model.entity.Emotions.INSENSITIVE.getEmotionUnicode()
            popupView.angry.text = potatocake.katecam.everymoment.data.model.entity.Emotions.ANGRY.getEmotionUnicode()
            popupView.confounded.text = potatocake.katecam.everymoment.data.model.entity.Emotions.CONFOUNDED.getEmotionUnicode()

            val clickListener: (potatocake.katecam.everymoment.data.model.entity.Emotions) -> Unit = { emotion ->
                onEmotionSelected(emotion)
                emotionPopup.dismiss()
            }

            popupView.happy.setOnClickListener { clickListener(potatocake.katecam.everymoment.data.model.entity.Emotions.HAPPY) }
            popupView.sad.setOnClickListener { clickListener(potatocake.katecam.everymoment.data.model.entity.Emotions.SAD) }
            popupView.insensitive.setOnClickListener { clickListener(potatocake.katecam.everymoment.data.model.entity.Emotions.INSENSITIVE) }
            popupView.angry.setOnClickListener { clickListener(potatocake.katecam.everymoment.data.model.entity.Emotions.ANGRY) }
            popupView.confounded.setOnClickListener { clickListener(potatocake.katecam.everymoment.data.model.entity.Emotions.CONFOUNDED) }

            return popupView
        }
}