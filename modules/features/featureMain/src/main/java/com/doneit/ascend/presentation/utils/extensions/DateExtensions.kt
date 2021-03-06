package com.doneit.ascend.presentation.utils.extensions

import android.content.Context
import android.text.format.DateFormat
import android.text.format.DateUtils
import com.doneit.ascend.domain.entity.MonthEntity
import com.doneit.ascend.domain.entity.getDefaultCalendar
import com.doneit.ascend.presentation.utils.Constants.AM
import com.doneit.ascend.presentation.utils.Constants.PM
import java.text.SimpleDateFormat
import java.util.*

fun Date.toYear(): Int {
    return this.toCalendar().get(Calendar.YEAR)
}

fun Date.toMonth(): Int {
    return this.toCalendar().get(Calendar.MONTH)
}

fun Date.toDayOfMonth(): Int {
    return this.toCalendar().get(Calendar.DAY_OF_MONTH)
}

fun Date.toCalendar(): Calendar {
    val calendar = getDefaultCalendar()
    calendar.time = this

    return calendar
}

fun Int.toTimeString(): String {
    return if (this < 10) "0$this" else this.toString()
}

fun Int.toAmPm(): String {
    return if (this == Calendar.AM) AM else PM
}

fun Int.toMonthEntity(): MonthEntity {
    return MonthEntity.values()[this]
}

fun Date.toDayFullMonthYear(): String {
    return SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH).getFormatted(this)
}

fun Date.toDayMonthYear(): String {
    return SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).getFormatted(this)
}

fun Date.toDayShortMonthYear(): String {
    return SimpleDateFormat("dd MM yyyy", Locale.ENGLISH).getFormatted(this)
}

fun Date.toNotificationDate(): String {
    return "MM.dd.yy hh:mm aa".toDefaultFormatter().getFormatted(this)
}

fun Date.toTimeStampFormat(): String {
    return TIMESTAMP_FORMAT.toDefaultFormatter().getFormatted(this)
}

fun Date.toChatDate(context: Context): String {
    val nowTime = Calendar.getInstance()
    val neededTime = Calendar.getInstance()
    neededTime.time = this
    return if (neededTime[Calendar.YEAR] == nowTime[Calendar.YEAR]) {
        return if (neededTime[Calendar.MONTH] == nowTime[Calendar.MONTH]) {
            if (nowTime[Calendar.DATE] == neededTime[Calendar.DATE]) {
                context.getTimeFormat().getFormatted(this)
            } else {
                "dd MMM".toDefaultFormatter().getFormatted(this)
            }
        } else {
            "dd MMM".toDefaultFormatter().getFormatted(this)
        }
    } else {
        "dd.MM.yyyy".toDefaultFormatter().getFormatted(this)
    }
}

fun Date.toAttachmentDate(): String {
    return "dd.MM.yy hh:mm aa".toDefaultFormatter().getFormatted(this)
}

fun Date.toRateDate(): String {
    return "MMM dd, yyyy".toDefaultFormatter().getFormatted(this)
}

fun Date.formatPostDate(): String {
    return when {
        isYesterday() -> formatYesterday()
        DateUtils.isToday(time) -> formatToday()
        else -> MESSAGE_FORMATTER.toDefaultFormatter().getFormatted(this)
    }
}

fun Date.isYesterday(): Boolean {
    val c1 = Calendar.getInstance()
    c1.add(Calendar.DAY_OF_YEAR, -1)

    val c2 = Calendar.getInstance()
    c2.time = this

    return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
            && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR))
}

fun Date.formatToday(): String {
    return "Today at " + HOUR_12_ONLY_FORMAT.toDefaultFormatter().format(this)
}

fun Date.formatYesterday(): String {
    return "Yesterday at " + HOUR_12_ONLY_FORMAT.toDefaultFormatter().format(this)
}

fun Date.toTimerFormat(): String {
    return "mm:ss".toDefaultFormatter().apply { timeZone = TimeZone.getTimeZone("GMT") }
        .getFormatted(this)
}

fun Date.toVideoChatTimerFormat(): String {
    return "HH:mm:ss".toDefaultFormatter().apply { timeZone = TimeZone.getTimeZone("GMT") }
        .getFormatted(this)
}

fun Date.toMinutesFormat(): String {
    return "mm' minutes'".toDefaultFormatter().getFormatted(this)
}

private fun SimpleDateFormat.getFormatted(date: Date): String {
    var res = ""
    try {
        res = this.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return res
}

fun String.toDefaultFormatter(): SimpleDateFormat {
    return SimpleDateFormat(this, Locale.ENGLISH)
}

fun String.toUSGMTFormatter(): SimpleDateFormat {
    val formatter = SimpleDateFormat(this, Locale.US)
    formatter.timeZone = TimeZone.getTimeZone("GMT")
    return formatter
}

fun String.toGMTFormatter(): SimpleDateFormat {
    val formatter = SimpleDateFormat(this, Locale.ENGLISH)
    formatter.timeZone = TimeZone.getTimeZone("GMT")
    return formatter
}

fun String.toLocaleTime(): Date {
    val cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
    return cal.apply {
        set(Calendar.HOUR_OF_DAY, substringBefore(":").toInt())
        set(Calendar.HOUR, substringBefore(":").toInt() % 12)
        set(Calendar.MINUTE, substringAfter(":").toInt())
        set(Calendar.AM_PM, if (substringBefore(":").toInt() < 12) Calendar.AM else Calendar.PM)
    }.time
}

fun String.toLocaleTimeString(context: Context): String {
    return toLocaleTime().run {
        context.getTimeFormat().format(this)
    }
}

fun Context.getTimeFormat(): SimpleDateFormat {
    return DateFormat.is24HourFormat(this).getTimeFormat()
}

fun Boolean.getTimeFormat(): SimpleDateFormat {
    return if (this) {
        HOUR_24_ONLY_FORMAT.toDefaultFormatter()
    } else {
        HOUR_12_ONLY_FORMAT.toDefaultFormatter()
    }
}

fun Context.getHoursByTimeZone(): List<String> {
    val list = mutableListOf<String>()
    if (DateFormat.is24HourFormat(this)) {
        for (n in 1..24) {
            list.add(n.toTimeString())
        }
    } else {
        for (n in 1..12) {
            list.add(n.toTimeString())
        }
    }
    return list
}

fun Context.getTimeType(): String {
    return if (DateFormat.is24HourFormat(this)) {
        "24"
    } else {
        "AM"
    }
}

fun getDefaultCalendar(): Calendar {
    return Calendar.getInstance()
}

fun calculateDate(currentMessageTime: Date, previousMessageTime: Date): Boolean {
    val current = getDefaultCalendar().apply { time = currentMessageTime }
    val previous = getDefaultCalendar().apply { time = previousMessageTime }
    return ((current.get(Calendar.YEAR) * 365 + current.get(Calendar.DAY_OF_YEAR)) -
            (previous.get(Calendar.YEAR) * 365 + previous.get(Calendar.DAY_OF_YEAR))) > 0
}

fun Long.getDurationBreakdown(): String? {
    val seconds = (this / 1000) % 60
    val minutes = (this / (1000 * 60) % 60)
    return String.format("%02d:%02d", minutes, seconds)
}

const val MESSAGE_FORMATTER = "dd MMMM yyyy"
const val START_TIME_FORMATTER = "dd MMMM yyyy"
const val TIME_24_FORMAT_DROP_DAY = "HH:mm"
const val TIME_12_FORMAT_DROP_DAY = "hh:mm a"
const val WEEK_ONLY_FORMAT = "EEE"
const val HOUR_12_ONLY_FORMAT = "hh:mm a"
const val HOUR_24_ONLY_FORMAT = "HH:mm"
const val FULL_DATE_FORMAT = "dd MMM yyyy hh:mm aa"
const val DAY_MONTH_FORMAT = "d MMM"
const val TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss"