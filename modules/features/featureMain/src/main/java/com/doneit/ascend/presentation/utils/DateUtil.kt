package com.doneit.ascend.presentation.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.toDayMonthYear(): String {
    return "dd MMM yyyy".toDefaultFormatter().getFormatted(this)
}

fun Date.toNotificationDate(): String {
    return "MM.dd.YY hh:mm aa".toDefaultFormatter().getFormatted(this)
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
    return SimpleDateFormat(this, Locale.getDefault())
}