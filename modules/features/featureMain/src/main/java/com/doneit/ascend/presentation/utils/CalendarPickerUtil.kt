package com.doneit.ascend.presentation.utils

import android.content.Context
import com.doneit.ascend.presentation.main.R

class CalendarPickerUtil(
    private val context: Context
) {

    fun getString(day: CalendarDay): String {
        val resId: Int = when (day) {
            CalendarDay.MONDAY -> R.string.mon
            CalendarDay.TUESDAY -> R.string.tue
            CalendarDay.WEDNESDAY -> R.string.wed
            CalendarDay.THURSDAY -> R.string.thu
            CalendarDay.FRIDAY -> R.string.fri
            CalendarDay.SATURDAY -> R.string.sat
            CalendarDay.SUNDAY -> R.string.sun
        }

        return context.getString(resId)
    }

    companion object {

        const val DEFAULT_TIME_TYPE = "AM"

        fun getHours(hours: Int): List<String> {
            val list = mutableListOf<String>()

            for (n in 0 until hours) {
                if (n < 10) {
                    list.add("0$n")
                } else {
                    list.add(n.toString())
                }
            }

            return list
        }

        fun getMinutes(): List<String> {
            val list = mutableListOf<String>()

            for (n in 0 until 60) {
                if (n < 10) {
                    list.add("0$n")
                } else {
                    list.add(n.toString())
                }
            }

            return list
        }

        fun getTimeType(): List<String> {
            return listOf("AM", "PM")
        }

        fun getHours(timeType: String): Int {
            return if (timeType == "PM") 24 else 12
        }
    }
}