package com.example.everymoment.extensions

import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.example.everymoment.R

class Bookmarks {
    companion object Bookmark {
        var isBookmarked: Boolean = false

        fun setBookmark(bookmarkId: ImageView) {
            if (isBookmarked == false) {
                bookmarkId.setImageResource(R.drawable.baseline_bookmark_border_24)
            } else {
                bookmarkId.setImageResource(R.drawable.baseline_bookmark_24)
            }
        }

        fun toggleBookmark(context: Context, bookmarkId: ImageView) {
            Log.d("bookmark", "bookmark clicked")
            if (isBookmarked == false) {
                bookmarkId.setImageResource(R.drawable.baseline_bookmark_24)
                Toast.makeText(
                    context,
                    context.getString(R.string.add_bookmark),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else {
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
}