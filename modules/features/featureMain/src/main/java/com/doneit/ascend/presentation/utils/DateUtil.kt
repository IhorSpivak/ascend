package com.doneit.ascend.presentation.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.toDayMonthYear(): String {
    var res = ""
    try {
        val resFormatter = "dd MMM yyyy".toDefaultFormatter()
        res = resFormatter.format(this)
    } catch (e: Exception){
        e.printStackTrace()
    }
    return res
}

fun Date.toNotificationDate(): String {
    var res = ""
    try {
        val formatter = "MM.dd.YY hh:mm aa".toDefaultFormatter()
        res = formatter.format(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return res
}

fun String.toDefaultFormatter(): SimpleDateFormat {
    return SimpleDateFormat(this, Locale.getDefault())
}