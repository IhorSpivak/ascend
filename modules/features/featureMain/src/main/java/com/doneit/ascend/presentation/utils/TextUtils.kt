package com.doneit.ascend.presentation.utils

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Patterns
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import com.doneit.ascend.domain.entity.getDefaultCalendar
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.models.LocationModel
import com.doneit.ascend.presentation.utils.extensions.toDefaultFormatter
import com.doneit.ascend.presentation.views.SmsCodeView
import java.text.ParseException
import java.util.*

fun Context.applyLinkStyle(source: SpannableString, start: Int, end: Int) {
    val spanStrategy = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    val color = ContextCompat.getColor(this, R.color.default_font_color)
    source.setSpan(StyleSpan(Typeface.BOLD), start, end, spanStrategy)
    source.setSpan(ForegroundColorSpan(color), start, end, spanStrategy)
}

fun ObservableField<String?>.getNotNull(): String {
    return get() ?: ""
}

fun ObservableField<String>.getNotNullString(): String {
    return get() ?: ""
}

fun ObservableField<Boolean>.getNotNull(): Boolean {
    return get() ?: false
}

fun String.isValidPassword(): Boolean {
    val r = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!\$%!@#£€*?&]{6,48}\$")
    return this.matches(r)
}

fun String.isValidName(): Boolean {
    val r = Regex("[a-zA-Z ]{4,}")
    return this.matches(r)
}

fun String.isValidCodeFromSms(): Boolean {
    return this.length in 1..4
}

fun String.isValidAnswer(): Boolean {
    val r = Regex("^[a-zA-Z\\s]{2,48}\$")
    return this.matches(r)
}

fun String.isValidConfirmationCode(): Boolean {
    return length == SmsCodeView.NUMBERS_COUNT
}

fun String.isValidGroupName(): Boolean {
    val r = Regex("^[a-zA-Z0-9\\s._-]{2,64}\$")
    return this.matches(r)
}

fun String.isValidMeetingsNumber(): Boolean {
    val r = Regex("^\\d{1,4}\$")
    return this.matches(r)
}

fun String.isValidPrice(): Boolean {
    val r = Regex("^\\d{1,6}(\\.\\d{1,4})?\$")
    return this.matches(r)
}

fun String.isDescriptionValid(): Boolean {
    val r = Regex("^[a-zA-Z0-9\\s_.]{2,1000}\$")
    return this.matches(r)
}
fun String.isThemeValid(): Boolean {
    val r = Regex("^[a-zA-Z0-9\\s_.]{2,64}\$")
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
        val date = "dd MMMM yyyy".toDefaultFormatter().parse(this)
        val today = getDefaultCalendar()
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

fun String.isValidStartDate(): Boolean {
    var res = false

    try {
        val date = ("dd MMMM yyyy".toDefaultFormatter().parse(this).time / 1000) % 60
        val today = (getDefaultCalendar().time.time / 1000) % DAYS_MOD

        if(date >= today) {
            res = true
        }
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return res
}
fun String.isValidActualTime(): Boolean {
    var res = false
    try {
        val date = ("dd MMMM yyyy".toDefaultFormatter().parse(this).time / 1000) % DAYS_MOD
        val today = (getDefaultCalendar().time.time / 1000) % DAYS_MOD

        if(date >= today) {
            res = true
        }
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return res
}

fun getLocation(city: String, country: String) : String {
    return "$city, $country"
}

fun String.toLocationModel(): LocationModel {
    val location = LocationModel()
    val data = this.split(',')

    if(data.isNotEmpty()) {
       location.city = data[0]
    }

    if(data.size > 1) {
        location.county = data[1].trim()
    }

    return location
}
const val DAYS_MOD = 86400