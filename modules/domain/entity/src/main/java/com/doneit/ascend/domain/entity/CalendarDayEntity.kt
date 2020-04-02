package com.doneit.ascend.domain.entity

enum class CalendarDayEntity {
    SUNDAY,
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY;

    override fun toString(): String {
        return super.toString().toLowerCase().capitalize()
    }
}