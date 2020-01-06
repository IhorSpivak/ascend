package com.doneit.ascend.presentation.models

data class PresentationChangePasswordModel(
    val currentPassword: ValidatableField = ValidatableField(),
    val newPassword: ValidatableField = ValidatableField(),
    val confirmPassword: ValidatableField = ValidatableField()
)