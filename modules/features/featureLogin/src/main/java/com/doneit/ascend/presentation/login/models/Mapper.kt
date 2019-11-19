package com.doneit.ascend.presentation.login.models

import com.doneit.ascend.domain.entity.SignUpModel

fun PresentationSignUpModel.toEntity(): SignUpModel {
    return SignUpModel(
        email,
        phoneCode + phone,
        name,
        password,
        passwordConfirmation,
        code
    )
}