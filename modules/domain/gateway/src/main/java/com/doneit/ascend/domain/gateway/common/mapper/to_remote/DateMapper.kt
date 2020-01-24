package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.gateway.common.mapper.Constants
import java.text.SimpleDateFormat
import java.util.*

fun Date.toRemoteString(): String {
    val formatter = SimpleDateFormat(Constants.REMOTE_DATE_FORMAT_FULL, Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("GMT")

    return formatter.format(this)
}

fun Date.toRemoteStringShort(): String {
    val formatter = SimpleDateFormat(Constants.REMOTE_DATE_FORMAT_SHORT, Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("GMT")

    return formatter.format(this)
}