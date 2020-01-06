package com.doneit.ascend.presentation.models

data class EditEmailModel(
    val email: ValidatableField = ValidatableField(),
    val password: ValidatableField = ValidatableField()
)