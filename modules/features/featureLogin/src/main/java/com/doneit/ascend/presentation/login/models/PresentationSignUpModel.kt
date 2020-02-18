package com.doneit.ascend.presentation.login.models

import androidx.databinding.ObservableField

class PresentationSignUpModel(
    var name: ValidatableField = ValidatableField(),
    var email: ValidatableField = ValidatableField(),
    var phoneCode: ObservableField<String> = ObservableField(""),
    var phone: ValidatableField = ValidatableField(),
    var password: ValidatableField = ValidatableField(),
    var passwordConfirmation: ValidatableField = ValidatableField(),
    var hasAgreed: ObservableField<Boolean> = ObservableField(true),
    var code: ValidatableField = ValidatableField()
) {
    fun getPhoneNumber(): String {
        return phoneCode.get() + phone.observableField.get()
    }

    fun clear() {
        name.observableField.set("")
        email.observableField.set("")
        phoneCode.set("")
        phone.observableField.set("")
        password.observableField.set("")
        passwordConfirmation.observableField.set("")
        hasAgreed.set(true)
        code.observableField.set("")
    }
}
