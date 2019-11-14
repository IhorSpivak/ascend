package com.doneit.ascend.domain.gateway.common.mapper.entities

import com.doneit.ascend.source.storage.remote.data.request.LoginRequest
import com.doneit.ascend.source.storage.remote.data.request.RegistrationRequest
import com.doneit.ascend.source.storage.remote.data.response.LoginResponse

fun com.doneit.ascend.domain.entity.LoginUserModel.toLoginRequest(): LoginRequest {
    return LoginRequest(
        number,
        password
    )
}