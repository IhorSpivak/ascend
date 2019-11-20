package com.doneit.ascend.presentation.login.utils

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import com.doneit.ascend.presentation.login.R


fun Context.applyLinkStyle(source: SpannableString, start: Int, end: Int) {
    val spanStrategy = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    val color = ContextCompat.getColor(this, R.color.default_font_color)
    source.setSpan(StyleSpan(Typeface.BOLD),start, end, spanStrategy)
    source.setSpan(ForegroundColorSpan(color), start, end, spanStrategy)
}

fun ObservableField<String>.getNotNull(): String {
    return get()?:""
}