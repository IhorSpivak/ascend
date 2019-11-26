package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.entity.LogInUserModel
import com.doneit.ascend.domain.entity.ResetPasswordModel
import com.doneit.ascend.domain.entity.SignUpModel
import com.doneit.ascend.source.storage.remote.data.request.LogInRequest
import com.doneit.ascend.source.storage.remote.data.request.ResetPasswordRequest
import com.doneit.ascend.source.storage.remote.data.request.SignUpRequest

fun LogInUserModel.toLoginRequest(): LogInRequest {
    return LogInRequest(
        number,
        password
    )
}

fun SignUpModel.toSignUpRequest(): SignUpRequest {
    return SignUpRequest(
        email,
        phone,
        name,
        password,
        passwordConfirmation,
        code
    )
}

fun ResetPasswordModel.toResetPasswordRequest(): ResetPasswordRequest {
    return ResetPasswordRequest(
        phone,
        code,
        password,
        passwordConfirmation
    )
}