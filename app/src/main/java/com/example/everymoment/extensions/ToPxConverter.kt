package com.example.everymoment.extensions

import android.content.res.Resources

class ToPxConverter {

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

}