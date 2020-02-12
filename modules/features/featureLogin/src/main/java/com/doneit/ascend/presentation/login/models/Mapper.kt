package com.doneit.ascend.presentation.login.models

import com.doneit.ascend.domain.entity.dto.LogInUserDTO
import com.doneit.ascend.domain.entity.dto.ResetPasswordDTO
import com.doneit.ascend.domain.entity.dto.SignUpDTO
import com.doneit.ascend.presentation.utils.getNotNullString

fun PresentationSignUpModel.toEntity(): SignUpDTO {
    return SignUpDTO(
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

fun PresentationNewPasswordModel.toEntity(): ResetPasswordDTO {
    return ResetPasswordDTO(
        phoneNumber,
        code.observableField.getNotNullString(),
        password.observableField.getNotNullString(),
        passwordConfirmation.observableField.getNotNullString()
    )
}

fun PresentationNewPasswordModel.toLogInModel(): LogInUserDTO {
    return LogInUserDTO(
        phoneNumber,
        password.observableField.getNotNullString()
    )
}