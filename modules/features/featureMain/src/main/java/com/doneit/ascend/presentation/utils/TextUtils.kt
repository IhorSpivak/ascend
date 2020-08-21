package com.doneit.ascend.presentation.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Patterns
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.ObservableField
import com.devs.readmoreoption.ReadMoreOption
import com.doneit.ascend.domain.entity.CalendarDayEntity
import com.doneit.ascend.domain.entity.getDefaultCalendar
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.entity.user.Community
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.models.LocationModel
import com.doneit.ascend.presentation.models.ValidatableField
import com.doneit.ascend.presentation.utils.extensions.START_TIME_FORMATTER
import com.doneit.ascend.presentation.utils.extensions.toDefaultFormatter
import com.doneit.ascend.presentation.views.MultilineEditWithError
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
    return get().orEmpty()
}

fun ObservableField<String>.getNotNullString(): String {
    return get().orEmpty()
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

fun String.isValidChatTitle(): Boolean {
    val r = Regex("^[a-zA-Z0-9\\s._-]{2,32}\$")
    return this.matches(r)
}

fun String.isValidMeetingsNumber(): Boolean {
    return this.isNullOrEmpty().not() && this.toInt() in 1..365
}

fun isValidValidatableList(list: List<ValidatableField>): Boolean {
    var result = true
    list.forEach {
        if (it.observableField.get()!!.isBlank()) {
            result = false
        }
    }
    return result
}

fun String.isValidPrice(): Boolean {
    val r = Regex("^\\d{1,6}(\\.\\d{1,4})?\$")
    return this.matches(r)
}

fun String.isDescriptionValid(): Boolean {
    val r = Regex("^[a-zA-Z0-9\\s_.]{2,500}\$")
    return this.matches(r) && isNotBlank()
}

fun String.isWebinarDescriptionValid(): Boolean {
    val r = Regex("^[a-zA-Z0-9\\s_.]{2,500}\$")
    return this.matches(r) && isNotBlank()
}

fun String.isThemeValid(): Boolean {
    val r = Regex("^[a-zA-Z0-9\\s_.]{2,32}\$")
    return this.matches(r)
}

fun String.isDayValid(dayList: List<CalendarDayEntity>): Boolean {

    return dayList.map { it.ordinal }.toSet().size == dayList.size
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
        val date = START_TIME_FORMATTER.toDefaultFormatter().parse(this)
        val today = getDefaultCalendar()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)

        if (date?.before(today.time) == false) {
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
        val date = (START_TIME_FORMATTER.toDefaultFormatter().parse(this).time / 1000) % 60
        val today = (getDefaultCalendar().time.time / 1000) % DAYS_MOD

        if (date >= today) {
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
        val date = (START_TIME_FORMATTER.toDefaultFormatter().parse(this).time / 1000) % DAYS_MOD
        val today = (getDefaultCalendar().time.time / 1000) % DAYS_MOD

        if (date >= today) {
            res = true
        }
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return res
}

fun getLocation(city: String, country: String): String {
    return "$city, $country"
}

fun String.toLocationModel(): LocationModel {
    val location = LocationModel()
    val data = this.split(',')

    if (data.isNotEmpty()) {
        location.city = data[0]
    }

    if (data.size > 1) {
        location.county = data[1].trim()
    }

    return location
}

fun convertGroupTypeToString(
    context: Context,
    community: String,
    type: GroupType?,
    isPrivate: Boolean
): String {
    fun privateOrPublic(@StringRes public: Int, @StringRes prvt: Int): Int {
        return if (isPrivate) {
            prvt
        } else public
    }

    fun supportOrMM(
        @StringRes publicSp: Int,
        @StringRes prvtSp: Int,
        @StringRes publicMM: Int,
        @StringRes prvtMM: Int
    ): Int {
        return when (type) {
            GroupType.SUPPORT -> privateOrPublic(publicSp, prvtSp)
            GroupType.INDIVIDUAL,
            GroupType.MASTER_MIND -> privateOrPublic(publicMM, prvtMM)
            else -> R.string.webinars
        }
    }

    val res = when (community) {
        Community.LIFESTYLE.title,
        Community.SPIRITUAL.title -> supportOrMM(
            R.string.public_collaboration_title,
            R.string.private_collaboration_title,
            R.string.group,
            R.string.individual
        )
        Community.RECOVERY.title -> supportOrMM(
            R.string.public_group_title,
            R.string.private_group_title,
            R.string.workshop,
            R.string.coaching
        )
        Community.FAMILY.title -> supportOrMM(
            R.string.public_group_title,
            R.string.private_group_title,
            R.string.group,
            R.string.individual
        )
        Community.SUCCESS.title,
        Community.INDUSTRY.title -> supportOrMM(
            R.string.public_collaboration_title,
            R.string.private_collaboration_title,
            R.string.workshop,
            R.string.coaching
        )
        else -> throw IllegalStateException("Unsupported community detected")
    }
    return context.getString(res)
}

fun convertCommunityToResId(community: String, type: GroupType?): Int {
    val titlePair = when (community) {
        Community.LIFESTYLE.title,
        Community.SPIRITUAL.title -> R.string.collaboration to R.string.group
        Community.RECOVERY.title -> R.string.group_title to R.string.workshop
        Community.FAMILY.title -> R.string.group_title to R.string.group
        Community.SUCCESS.title,
        Community.INDUSTRY.title -> R.string.collaboration to R.string.workshop
        else -> throw IllegalStateException("Unsupported community detected")
    }
    return when (type) {
        GroupType.INDIVIDUAL,
        GroupType.MASTER_MIND -> titlePair.second
        GroupType.SUPPORT -> titlePair.first
        else -> R.string.webinars
    }
}

fun TextView.addReadMoreTo( text: String) {
    val readMoreOption = ReadMoreOption.Builder(this.context)
        .textLength(300, ReadMoreOption.TYPE_CHARACTER)
        .moreLabel(resources.getString(R.string.read_more))
        .moreLabelColor(Color.BLACK)
        .lessLabelColor(Color.WHITE)
        .labelUnderLine(true)
        .expandAnimation(true)
        .build()

    readMoreOption.addReadMoreTo(this, text)
}

fun applyMultilineFilter(editText: MultilineEditWithError) {
    editText.addOnTextChangedListener { text ->
        if (text.startsWith(" ") || text.startsWith("\r") ||
            text.startsWith("\n")
        ) {
            editText.text = text.trim()
        }
    }
}

fun EditText.applyFilter() {
    this.doAfterTextChanged { text ->
        if (text.toString().startsWith(" ") || text.toString().startsWith("\r") ||
            text.toString().startsWith("\n")
        ) {
            this.setText(text.toString().trim())
        }
    }
}

const val DAYS_MOD = 86400