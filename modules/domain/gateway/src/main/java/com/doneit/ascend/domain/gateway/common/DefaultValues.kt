package com.doneit.ascend.domain.gateway.common

import java.text.SimpleDateFormat
import java.util.*

fun String.toDefaultFormatter(): SimpleDateFormat {
    val formatter = SimpleDateFormat(this, Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("GMT")
    return formatter
}