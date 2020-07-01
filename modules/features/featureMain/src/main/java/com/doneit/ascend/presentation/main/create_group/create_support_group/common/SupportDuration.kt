package com.doneit.ascend.presentation.main.create_group.create_support_group.common

enum class SupportDuration(
    val time: Int,
    val label: String
) {
    DURATION(-1, "Duration"),
    HOUR(60, "1 hour"),
    HOUR_FIFTEEN(75, "1h 15min"),
    HOUR_THIRTY(90, "1h 30min"),
    HOUR_FORTY_FIVE(105, "1h 45min"),
    TWO_HOURS(120, "2 hours"),
    THREE_HOURS(180, "3 hours"),
    FOUR_HOURS(240, "4 hours"),
    FIVE_HOURS(300, "5 hours"),
    SIX_HOURS(360, "6 hours"),
    SEVEN_HOURS(420, "7 hours"),
    EIGHT_HOUR(480, "8 hours");

    override fun toString(): String {
        return label
    }

    companion object {
        fun fromDuration(time: Int): SupportDuration {
            return values()
                .firstOrNull { it.time == time } ?: DURATION
        }
    }
}