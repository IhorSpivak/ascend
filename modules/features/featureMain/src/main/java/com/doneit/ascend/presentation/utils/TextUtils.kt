package com.doneit.ascend.presentation.utils

import android.util.Patterns
import androidx.databinding.ObservableField

fun ObservableField<String?>.getNotNull(): String {
    return get() ?: ""
}

fun String.isValidGroupName(): Boolean {
    val r = Regex("^[a-zA-Z0-9._-]{2,64}\$")
    return this.matches(r)
}

fun String.isValidStartDate(): Boolean {
    val r = Regex("^\\d{2}-\\d{2}-\\d{4}\$")
    return this.matches(r)
}

fun String.isValid4Number(): Boolean {
    val r = Regex("^\\d{1,4}\$")
    return this.matches(r)
}

fun String.isDescriptionValid(): Boolean {
    val r = Regex("^[a-zA-Z0-9\\s_.]{2,1000}\$")
    return this.matches(r)
}

fun String.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches() && this.length <= 48
}

fun String.getFileExtension(): String {
    return substring(lastIndexOf("."))
}