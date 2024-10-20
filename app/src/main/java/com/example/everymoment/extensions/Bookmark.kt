package com.example.everymoment.extensions

import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.example.everymoment.R

class Bookmark(private val context: Context, private val bookmarkId: ImageView) {

    private var isBookmarked: Boolean = false

    fun setBookmark(isBookmarked: Boolean) {
        this.isBookmarked = isBookmarked
        if (!isBookmarked) {
            bookmarkId.setImageResource(R.drawable.baseline_bookmark_border_24)
        } else {
            bookmarkId.setImageResource(R.drawable.baseline_bookmark_24)
        }
    }

    fun toggleBookmark() {
        Log.d("bookmark", "bookmark clicked")
        if (!isBookmarked) {
            bookmarkId.setImageResource(R.drawable.baseline_bookmark_24)
            Toast.makeText(
                context,
                context.getString(R.string.add_bookmark),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            bookmarkId.setImageResource(R.drawable.baseline_bookmark_border_24)
            Toast.makeText(
                context,
                context.getString(R.string.remove_bookmark),
                Toast.LENGTH_SHORT
            ).show()
        }
        isBookmarked = !isBookmarked
    }
}