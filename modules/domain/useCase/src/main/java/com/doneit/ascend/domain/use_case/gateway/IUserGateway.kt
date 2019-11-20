package com.doneit.ascend.domain.use_case.gateway

import com.doneit.ascend.domain.entity.AuthEntity
import com.doneit.ascend.domain.entity.LogInUserModel
import com.doneit.ascend.domain.entity.common.RequestEntity
import com.doneit.ascend.domain.entity.SignUpModel

interface IUserGateway {
    suspend fun signIn(loginMode: LogInUserModel): RequestEntity<AuthEntity, List<String>>

    suspend fun signUp(signUpModel: SignUpModel): RequestEntity<AuthEntity, List<String>>

    suspend fun getConfirmationCode(phone: String): RequestEntity<Unit, List<String>>
}