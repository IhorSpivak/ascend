package com.doneit.ascend.domain.entity.common

data class ResponseEntity<T, E>(
    val isSuccessful: Boolean,
    val successModel: T?,
    val errorModel: E?
)