package com.doneit.ascend.presentation.main.models

data class ValidationResult(
    var isSussed: Boolean = true,
    val errors: MutableList<Int> = mutableListOf()
)