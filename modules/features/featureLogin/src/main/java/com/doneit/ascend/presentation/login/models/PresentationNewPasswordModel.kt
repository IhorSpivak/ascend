package com.doneit.ascend.presentation.login.models

class PresentationNewPasswordModel(
    var phoneNumber: String = "",
    var code: ValidatableField = ValidatableField(),
    var password: ValidatableField = ValidatableField(),
    var passwordConfirmation: ValidatableField = ValidatableField()
) {
    fun clear() {
        phoneNumber = ""
        code.observableField.set("")
        password.observableField.set("")
        passwordConfirmation.observableField.set("")
    }
}