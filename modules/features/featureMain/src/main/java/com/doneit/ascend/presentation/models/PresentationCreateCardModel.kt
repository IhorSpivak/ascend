package com.doneit.ascend.presentation.models

data class PresentationCreateCardModel(
    val cardNumber: ValidatableField = ValidatableField(),
    val name: ValidatableField = ValidatableField(),
    val cvv: ValidatableField = ValidatableField(),
    val expiration: ValidatableField = ValidatableField()
)