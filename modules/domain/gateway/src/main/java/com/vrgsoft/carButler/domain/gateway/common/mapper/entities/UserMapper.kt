package com.vrgsoft.carButler.domain.gateway.common.mapper.entities

import com.vrgsoft.carButler.domain.entity.LoginUserModel
import com.vrgsoft.carButler.source.storage.remote.data.request.LoginRequest
import com.vrgsoft.carButler.source.storage.remote.data.request.RegistrationRequest
import com.vrgsoft.carButler.source.storage.remote.data.response.LoginResponse

fun LoginUserModel.toLoginRequest(): LoginRequest {
    return LoginRequest(
        number,
        password
    )
}