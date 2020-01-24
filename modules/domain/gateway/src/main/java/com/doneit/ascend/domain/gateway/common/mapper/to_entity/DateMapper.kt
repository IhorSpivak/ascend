package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.gateway.common.mapper.Constants
import java.text.SimpleDateFormat
import java.util.*

fun String.toDate(): Date? {
    val formatter = SimpleDateFormat(Constants.REMOTE_DATE_FORMAT_FULL, Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("GMT")

    return formatter.parse(this)
}

fun String.toShortDate(): Date? {
    val formatter = SimpleDateFormat(Constants.REMOTE_DATE_FORMAT_SHORT, Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("GMT")

    return formatter.parse(this)
}
