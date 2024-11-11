package potatocake.katecam.everymoment.extensions

import android.util.Log
import android.widget.ImageView
import potatocake.katecam.everymoment.R

class Like(private val likeId: ImageView) {

    private var isLiked: Boolean = false

    fun setLike(isLiked: Boolean) {
        this.isLiked = isLiked
        if (!isLiked) {
            likeId.setImageResource(R.drawable.favorite_24px)
        } else {
            likeId.setImageResource(R.drawable.favorite_fill_24px)
        }
    }

    fun checkIsLike(): Boolean {
        return isLiked
    }

    fun toggleLike() {
        Log.d("like", "like clicked")
        if (!isLiked) {
            likeId.setImageResource(R.drawable.favorite_24px)
        } else {
            likeId.setImageResource(R.drawable.favorite_fill_24px)
        }
        isLiked = !isLiked
    }
}