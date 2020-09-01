package com.doneit.ascend.presentation.main.create_group.create_support_group.common

import androidx.annotation.StringRes
import com.doneit.ascend.presentation.main.R

enum class SupportDuration(
    val time: Int,
    @StringRes val label: Int
) {
    DURATION(-1, R.string.duration),
    HOUR(60, R.string.one_hour),
    HOUR_FIFTEEN(75, R.string.one_hour_fifteen_minutes),
    HOUR_THIRTY(90, R.string.one_hour_thirty_minutes),
    HOUR_FORTY_FIVE(105, R.string.one_hour_forty_five_minutes),
    TWO_HOURS(120, R.string.two_hours),
    THREE_HOURS(180, R.string.three_hours),
    FOUR_HOURS(240, R.string.four_hours),
    FIVE_HOURS(300, R.string.five_hours),
    SIX_HOURS(360, R.string.six_hours),
    SEVEN_HOURS(420, R.string.seven_hours),
    EIGHT_HOUR(480, R.string.eight_hours);

    companion object {
        fun fromDuration(time: Int): SupportDuration {
            return values()
                .firstOrNull { it.time == time } ?: DURATION
        }
    }
}