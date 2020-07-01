package com.doneit.ascend.presentation.main.create_group.master_mind.common

enum class Duration(
    val time: Int,
    val label: String
) {
    DURATION(-1, "Duration"),
    HOUR(60, "1 hour"),
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
        fun fromDuration(time: Int): Duration {
            return values()
                .firstOrNull { it.time == time } ?: DURATION
        }
    }
}