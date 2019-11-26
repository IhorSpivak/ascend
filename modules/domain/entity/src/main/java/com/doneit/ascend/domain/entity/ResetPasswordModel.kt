package com.doneit.ascend.domain.entity

data class ResetPasswordModel(
    val phone: String,
    val code: String,
    val password: String,
    val passwordConfirmation: String
)