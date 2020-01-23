package com.doneit.ascend.presentation.models

data class PresentationCreateCardModel(
    val cardNumber: ValidatableField = ValidatableField(),
    val name: ValidatableField = ValidatableField(),
    val cvv: ValidatableField = ValidatableField(),
    val expiration: ValidatableField = ValidatableField()
) {
    override fun equals(other: Any?): Boolean {
        return this === other
    }

    override fun hashCode(): Int {
        return cardNumber.hashCode() + name.hashCode() + cvv.hashCode() + expiration.hashCode()
    }
}