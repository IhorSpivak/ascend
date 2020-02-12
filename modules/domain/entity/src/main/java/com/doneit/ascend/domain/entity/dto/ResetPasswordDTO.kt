package com.doneit.ascend.domain.entity.dto

data class ResetPasswordDTO(
    val phone: String,
    val code: String,
    val password: String,
    val passwordConfirmation: String
)