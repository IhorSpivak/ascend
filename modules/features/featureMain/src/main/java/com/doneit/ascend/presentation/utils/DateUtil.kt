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