package com.doneit.ascend.presentation.models

data class ValidationResult(
    var isSucceed: Boolean = true,
    val errors: MutableList<Int> = mutableListOf()
)