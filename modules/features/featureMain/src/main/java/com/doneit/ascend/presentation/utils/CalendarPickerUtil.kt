package com.doneit.ascend.presentation.utils

class CalendarPickerUtil {

    companion object {
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