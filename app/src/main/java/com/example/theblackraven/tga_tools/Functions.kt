package com.example.theblackraven.tga_tools

import android.util.DisplayMetrics
import android.util.TypedValue

fun convertDpToPx(dp: Int, displayMetrics: DisplayMetrics): Int {
    val pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), displayMetrics)
    return Math.round(pixels)
}

