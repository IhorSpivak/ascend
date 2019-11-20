package com.doneit.ascend.presentation.login.models

import com.doneit.ascend.domain.entity.SignUpModel
import com.doneit.ascend.presentation.login.utils.getNotNull

fun PresentationSignUpModel.toEntity(): SignUpModel {
    return SignUpModel(
        email.observableField.getNotNull(),
        phoneCode + phone.observableField.getNotNull(),
        name.observableField.get()?:"",
        password.observableField.getNotNull(),
        passwordConfirmation.observableField.getNotNull(),
        code
    )
}