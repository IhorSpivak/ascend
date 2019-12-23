package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import java.text.SimpleDateFormat
import java.util.*

private const val REMOTE_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"//considered always to have 0 timezone offset

fun Date.toRemoteString(): String {
    val formatter = SimpleDateFormat(REMOTE_DATE_FORMAT, Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("GMT")

    return formatter.format(this)
}