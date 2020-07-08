package com.doneit.ascend.domain.entity.common

data class BaseCallback<T>(
    val onSuccess: (T) -> Unit,
    val onError: (String) -> Unit
)