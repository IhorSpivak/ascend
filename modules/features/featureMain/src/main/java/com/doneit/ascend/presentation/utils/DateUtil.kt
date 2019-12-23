package com.doneit.ascend.presentation.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.toDayMonthYear(): String {
    var res = ""
    try {
        val resFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        res = resFormatter.format(this)
    } catch (e: Exception){
        e.printStackTrace()
    }
    return res
}

fun Date.toNotificationDate(): String {
    var res = ""
    try {
        val formatter = SimpleDateFormat("MM.dd.YY hh:mm aa", Locale.getDefault())
        res = formatter.format(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return res
}