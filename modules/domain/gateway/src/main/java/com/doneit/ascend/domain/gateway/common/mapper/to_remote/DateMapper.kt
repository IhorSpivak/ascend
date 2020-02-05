package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.gateway.common.mapper.Constants
import com.doneit.ascend.domain.gateway.common.toDefaultFormatter
import java.util.*

fun Date.toRemoteString(): String {
    return Constants.REMOTE_DATE_FORMAT_FULL.toDefaultFormatter().format(this)
}

fun Date.toRemoteStringShort(): String {
    return Constants.REMOTE_DATE_FORMAT_SHORT.toDefaultFormatter().format(this)
}