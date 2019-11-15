package com.doneit.ascend.domain.use_case.gateway

import com.doneit.ascend.domain.entity.LoginUserModel
import com.doneit.ascend.domain.entity.User

interface IUserGateway {
    suspend fun login(registerModel: LoginUserModel): User?
}