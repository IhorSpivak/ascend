package com.doneit.ascend.domain.entity.dto

data class ChangePasswordDTO(
    val currentPassword: String,
    val password: String,
    val passwordConfirmation: String
)