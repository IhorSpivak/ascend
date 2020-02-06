package com.doneit.ascend.domain.gateway.common

import com.doneit.ascend.domain.gateway.common.mapper.Constants
import java.text.SimpleDateFormat
import java.util.*

fun String.toDefaultFormatter(): SimpleDateFormat {
    val formatter = SimpleDateFormat(this, Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("GMT")
    return formatter
}

fun Date.getDayOffset(): Int {
    val calendar = Calendar.getInstance()//with timezone
    calendar.time = this
    val localDay = calendar.get(Calendar.DAY_OF_WEEK)

    calendar.timeZone = TimeZone.getTimeZone("GMT")//set utc interpretation
    val utcDay = calendar.get(Calendar.DAY_OF_WEEK)

    return utcDay - localDay
}

fun List<Int>.applyDaysOffset(dayOffset: Int): List<Int> {
    return this.map {
        var res = it + dayOffset
        while(res < 0){
            res += Constants.DAYS_IN_WEEK_COUNT
        }

        res % Constants.DAYS_IN_WEEK_COUNT
    }
}