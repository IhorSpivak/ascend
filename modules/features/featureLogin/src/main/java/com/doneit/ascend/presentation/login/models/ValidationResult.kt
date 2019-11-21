package com.doneit.ascend.presentation.login.models

data class ValidationResult(
    var isSussed: Boolean = true,
    val errors: MutableList<Int> = mutableListOf()
)