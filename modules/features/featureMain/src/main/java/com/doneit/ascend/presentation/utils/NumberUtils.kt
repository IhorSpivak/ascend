package com.doneit.ascend.presentation.utils

import kotlin.math.abs

fun Int.formatToKValue(): String {
    return when {
        abs(this / 1000000) > 1 -> {
            (this / 1000000).toString() + "M"
        }
        abs(this / 1000) > 1 -> {
            (this / 1000).toString() + "k"
        }
        else -> {
            this.toString()
        }
    }
}