package com.doneit.ascend.presentation.login.utils

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Patterns
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.views.SmsCodeView


fun Context.applyLinkStyle(source: SpannableString, start: Int, end: Int) {
    val spanStrategy = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    val color = ContextCompat.getColor(this, R.color.default_font_color)
    source.setSpan(StyleSpan(Typeface.BOLD),start, end, spanStrategy)
    source.setSpan(ForegroundColorSpan(color), start, end, spanStrategy)
}

fun ObservableField<String>.getNotNull(): String {
    return get()?:""
}

fun ObservableField<Boolean>.getNotNull(): Boolean {
    return get()?:false
}

fun String.isValidPassword(): Boolean {
    val r = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!\$%!@#£€*?&]{6,}\$")
    return this.matches(r)
}

fun String.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidName(): Boolean {
    val r = Regex("[a-zA-Z ]{4,}")
    return this.matches(r)
}

fun String.isValidConfirmationCode(): Boolean {
    return length == SmsCodeView.NUMBERS_COUNT
}

fun String.isValidAnswer(): Boolean {
    val r = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!\$%!@#£€*?&]{6,}\$")
    return this.length in 3..48 && this.matches(r)
}