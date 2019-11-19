package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.entity.LoginUserModel
import com.doneit.ascend.source.storage.remote.data.request.LoginRequest

fun LoginUserModel.toLoginRequest(): LoginRequest {
    return LoginRequest(
        number,
        password
    )
}