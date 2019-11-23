package com.doneit.ascend.presentation.login.models

import androidx.databinding.ObservableField

class PresentationSignUpModel(
    var email: ValidatableField = ValidatableField(),
    var phoneCode: ObservableField<String> = ObservableField(""),
    var phone: ValidatableField = ValidatableField(),
    var name: ValidatableField = ValidatableField(),
    var password: ValidatableField = ValidatableField(),
    var passwordConfirmation: ValidatableField = ValidatableField(),
    var hasAgreed: ObservableField<Boolean> = ObservableField(false),
    var code: ValidatableField = ValidatableField()
) {
    fun getPhoneNumber(): String {
        return phoneCode.get() + phone.observableField.get()
    }
}
