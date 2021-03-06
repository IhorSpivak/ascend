package com.doneit.ascend.presentation.utils

import android.content.Context
import android.text.format.DateFormat
import com.doneit.ascend.domain.entity.CalendarDayEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.utils.Constants.AM
import com.doneit.ascend.presentation.utils.extensions.toTimeString

class CalendarPickerUtil(
    private val context: Context
) {

    fun getString(day: CalendarDayEntity): String {
        val resId: Int = when (day) {
            CalendarDayEntity.MONDAY -> R.string.mon
            CalendarDayEntity.TUESDAY -> R.string.tue
            CalendarDayEntity.WEDNESDAY -> R.string.wed
            CalendarDayEntity.THURSDAY -> R.string.thu
            CalendarDayEntity.FRIDAY -> R.string.fri
            CalendarDayEntity.SATURDAY -> R.string.sat
            CalendarDayEntity.SUNDAY -> R.string.sun
        }

        return context.getString(resId)
    }

    companion object {

        const val DEFAULT_TIME_TYPE = AM

        fun getHours(): List<String> {
            val list = mutableListOf<String>()

            for (n in 1..12) {
                list.add(n.toTimeString())
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
    }
    fun is24TimeFormat(): Boolean{
        return DateFormat.is24HourFormat(context)
    }
}