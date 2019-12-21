package com.doneit.ascend.presentation.utils

import android.util.Patterns
import androidx.databinding.ObservableField
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun ObservableField<String?>.getNotNull(): String {
    return get() ?: ""
}

fun String.isValidGroupName(): Boolean {
    val r = Regex("^[a-zA-Z0-9\\s._-]{2,64}\$")
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

fun String.isValidStrartDate(): Boolean {
    var res = false

    try {
        val date = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).parse(this)
        val today = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)

        if(date?.before(today.time) == false) {
            res = true
        }
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return res
}