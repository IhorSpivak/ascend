package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.gateway.common.mapper.Constants
import com.doneit.ascend.domain.gateway.common.toDefaultFormatter
import java.util.*
import java.util.concurrent.TimeUnit

fun Date.toRemoteString(): String {
    return Constants.REMOTE_DATE_FORMAT_FULL.toDefaultFormatter().format(this)
}

fun Date.toRemoteStringShort(): String {
    return Constants.REMOTE_DATE_FORMAT_SHORT.toDefaultFormatter().format(this)
}

fun Long.toGMTTimestamp(): Long {
    val mCalendar: Calendar = GregorianCalendar()
    val mTimeZone = mCalendar.timeZone
    val mGMTOffset = mTimeZone.rawOffset.toLong() + if (mTimeZone.inDaylightTime(Date())) {
        mTimeZone.dstSavings
    } else 0
    val time = this - TimeUnit.MINUTES.convert(mGMTOffset, TimeUnit.MILLISECONDS)
    return if (time < 0) {
        24 * 60 + time
    } else {
        time
    }
}