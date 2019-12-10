package com.doneit.ascend.presentation.utils

import android.content.Context
import com.doneit.ascend.presentation.main.R

class DatePickerUtil(
    private val context: Context
) {

    private val month = mapOf(
        1 to context.getString(R.string.january),
        2 to context.getString(R.string.february),
        3 to context.getString(R.string.march),
        4 to context.getString(R.string.april),
        5 to context.getString(R.string.may),
        6 to context.getString(R.string.june),
        7 to context.getString(R.string.july),
        8 to context.getString(R.string.august),
        9 to context.getString(R.string.september),
        10 to context.getString(R.string.october),
        11 to context.getString(R.string.november),
        12 to context.getString(R.string.december)
    )

    fun getMonthList(): List<String> {
        return month.values.toList()
    }

    fun getStringValue(key: Int): String {
        return month[key] ?: ""
    }

    fun getNumberValue(value: String): Int {
        for (pair in month.entries) {
            if (pair.value == value) {
                return pair.key
            }
        }

        return -1
    }
}