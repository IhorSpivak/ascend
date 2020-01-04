package com.doneit.ascend.presentation.models

import androidx.databinding.ObservableField

data class EditPhoneModel(
    val phoneCode: ObservableField<String?> = ObservableField(""),
    val phoneNumber: ValidatableField = ValidatableField(),
    val password: ValidatableField = ValidatableField(),
    val code: ValidatableField = ValidatableField()
)