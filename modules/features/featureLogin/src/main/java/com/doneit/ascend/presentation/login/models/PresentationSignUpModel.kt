package com.doneit.ascend.presentation.login.models

class PresentationSignUpModel(
    var email: ValidatableField = ValidatableField(),
    var phoneCode: String = "",
    var phone: ValidatableField = ValidatableField(),
    var name: ValidatableField = ValidatableField(),
    var password: ValidatableField = ValidatableField(),
    var passwordConfirmation: ValidatableField = ValidatableField(),
    var code: String = ""
) {
    fun getPhoneNumber(): String {
        return phoneCode + phone
    }
}
