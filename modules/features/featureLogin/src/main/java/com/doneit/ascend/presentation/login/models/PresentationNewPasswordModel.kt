package com.doneit.ascend.presentation.login.models

class PresentationNewPasswordModel(
    var code: ValidatableField = ValidatableField(),
    var password: ValidatableField = ValidatableField(),
    var passwordConfirmation: ValidatableField = ValidatableField()
)