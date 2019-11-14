package com.doneit.ascend.domain.gateway.common

fun Long?.orDefault(): Long {
    return this ?: -1L
}

fun Int?.orDefault(): Int {
    return this ?: 0
}