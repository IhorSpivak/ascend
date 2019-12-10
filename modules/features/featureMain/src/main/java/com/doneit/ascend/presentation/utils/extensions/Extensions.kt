package com.doneit.ascend.presentation.utils.extensions

import com.doneit.ascend.presentation.utils.Constants

fun List<String>.toErrorMessage(): String {
    val res = StringBuilder()
    forEach {
        res.append(Constants.TAB_SYMBOL)
        res.append(it)
        res.append(Constants.END_OF_LINE)
    }
    if (res.isNotEmpty()) {
        res.setLength(res.length - 1)
    }
    return res.toString()
}