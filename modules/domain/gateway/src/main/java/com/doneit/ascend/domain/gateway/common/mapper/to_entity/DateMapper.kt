package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import java.text.SimpleDateFormat
import java.util.*

private const val REMOTE_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

fun String.toDate(): Date? {
    return SimpleDateFormat(REMOTE_DATE_FORMAT, Locale.getDefault()).parse(this)
}