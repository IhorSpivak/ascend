package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.gateway.common.mapper.Constants
import com.doneit.ascend.domain.gateway.common.toDefaultFormatter
import java.util.*

fun String.toDate(): Date {
    return Constants.REMOTE_DATE_FORMAT_FULL.toDefaultFormatter().parse(this) ?: Date()
}

fun String.toShortDate(): Date? {
    return Constants.REMOTE_DATE_FORMAT_SHORT.toDefaultFormatter().parse(this)
}
