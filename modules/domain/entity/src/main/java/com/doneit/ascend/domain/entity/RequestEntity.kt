package com.doneit.ascend.domain.entity

data class RequestEntity<T, E>(
    val isSuccessful: Boolean,
    val code: Int,
    val message: String,
    val successModel: T?,
    val errorModel: E?
)