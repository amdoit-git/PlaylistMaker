package com.example.playlistmaker.ui

import android.content.Context
import android.util.TypedValue

interface DpToPx {
    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics
        ).toInt()
    }
}