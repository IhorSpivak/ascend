package com.doneit.ascend.presentation.utils.extensions

import android.content.Context
import android.text.format.DateFormat
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

fun Date.toDayMonthYear(): String {
    return SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).getFormatted(this)
}

fun Date.toNotificationDate(): String {
    return "MM.dd.yy hh:mm aa".toDefaultFormatter().getFormatted(this)
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

fun Date.toTimerFormat(): String {
    return "mm:ss".toDefaultFormatter().getFormatted(this)
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

fun String.toGMTFormatter(): SimpleDateFormat {
    val formatter = SimpleDateFormat(this, Locale.ENGLISH)
    formatter.timeZone = TimeZone.getTimeZone("GMT")
    return formatter
}

fun Context.getTimeFormat(): SimpleDateFormat {
    return if (DateFormat.is24HourFormat(this)) {
        "HH:mm".toDefaultFormatter()
    } else {
        "hh:mm aa".toDefaultFormatter()
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

fun calculateDate(currentMessageTime: Date, previousMessageTime: Date): Boolean {
    val current = getDefaultCalendar().apply { time = currentMessageTime }
    val previous = getDefaultCalendar().apply { time = previousMessageTime }
    return ((current.get(Calendar.YEAR) * 365 + current.get(Calendar.DAY_OF_YEAR)) -
            (previous.get(Calendar.YEAR) * 365 + previous.get(Calendar.DAY_OF_YEAR))) > 0
}
    val START_TIME_FORMATTER = "dd MMMM yyyy".toDefaultFormatter().apply{ timeZone = TimeZone.getTimeZone("GMT") }
    val TIME_24_FORMAT = "EEE, HH:mm".toDefaultFormatter()
    val TIME_12_FORMAT = "EEE, hh:mm a".toDefaultFormatter()
    val TIME_24_FORMAT_DROP_DAY = "HH:mm".toDefaultFormatter()
    val TIME_12_FORMAT_DROP_DAY = "hh:mm a".toDefaultFormatter()
    val WEEK_ONLY_FORMAT = "EEE".toDefaultFormatter()
    val HOUR_12_ONLY_FORMAT = "hh:mm a".toDefaultFormatter().apply{ timeZone = TimeZone.getTimeZone("GMT") }
    val HOUR_24_ONLY_FORMAT = "HH:mm".toDefaultFormatter().apply{ timeZone = TimeZone.getTimeZone("GMT") }