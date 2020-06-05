package com.doneit.ascend.domain.gateway.common

fun String?.defaultValue(): String {
    return this ?: ""
}