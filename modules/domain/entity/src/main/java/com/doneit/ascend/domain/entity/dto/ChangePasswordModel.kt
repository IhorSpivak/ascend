package com.doneit.ascend.domain.entity.dto

data class ChangePasswordModel(
    val currentPassword: String,
    val password: String,
    val passwordConfirmation: String
)