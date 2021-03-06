package com.doneit.ascend.presentation.utils

fun <T> List<T>.insert(position: Int= 0, element: T): List<T> {
    val res = this.toMutableList()
    res.add(position, element)
    return res
}

fun <T> List<T>.append(element: T): List<T> {
    val res = this.toMutableList()
    res.add(element)
    return res
}