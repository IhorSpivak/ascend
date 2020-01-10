package com.doneit.ascend.presentation.login.models

import com.doneit.ascend.domain.entity.dto.LogInUserModel
import com.doneit.ascend.domain.entity.dto.ResetPasswordModel
import com.doneit.ascend.domain.entity.dto.SignUpModel
import com.doneit.ascend.presentation.utils.getNotNullString

fun PresentationSignUpModel.toEntity(): SignUpModel {
    return SignUpModel(
        email.observableField.getNotNullString(),
        getPhoneNumber(),
        name.observableField.get() ?: "",
        password.observableField.getNotNullString(),
        passwordConfirmation.observableField.getNotNullString(),
        code.observableField.getNotNullString()
    )
}

fun PresentationPhoneModel.toEntity(): String {
    return getPhoneNumber()
}

fun PresentationNewPasswordModel.toEntity(): ResetPasswordModel {
    return ResetPasswordModel(
        phoneNumber,
        code.observableField.getNotNullString(),
        password.observableField.getNotNullString(),
        passwordConfirmation.observableField.getNotNullString()
    )
}

fun PresentationNewPasswordModel.toLogInModel(): LogInUserModel {
    return LogInUserModel(
        phoneNumber,
        password.observableField.getNotNullString()
    )
}