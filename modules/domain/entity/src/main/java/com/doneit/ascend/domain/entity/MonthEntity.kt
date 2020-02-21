package com.doneit.ascend.domain.entity

enum class MonthEntity {
    JANUARY,
    FEBRUARY,
    MARCH,
    APRIL,
    MAY,
    JUNE,
    JULY,
    AUGUST,
    SEPTEMBER,
    OCTOBER,
    NOVEMBER,
    DECEMBER;

    override fun toString(): String {
        return super.toString().toLowerCase().capitalize()
    }

    fun toNumeric(): Int {
        return ordinal + 1
    }
}