package com.doneit.ascend.presentation.models

data class ValidationResult(
    var isSussed: Boolean = true,
    val errors: MutableList<Int> = mutableListOf()
)