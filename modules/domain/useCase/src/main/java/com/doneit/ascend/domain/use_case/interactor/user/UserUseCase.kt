package com.doneit.ascend.domain.use_case.interactor.user

import com.doneit.ascend.domain.entity.AuthEntity
import com.doneit.ascend.domain.entity.LogInUserModel
import com.doneit.ascend.domain.entity.SignUpModel
import com.doneit.ascend.domain.entity.common.RequestEntity

interface UserUseCase {
    suspend fun signIn(logInModel: LogInUserModel): RequestEntity<AuthEntity, List<String>>

    suspend fun signUp(registerModel: SignUpModel): RequestEntity<AuthEntity, List<String>>

    suspend fun getConfirmationCode(phone: String): RequestEntity<Unit, List<String>>
}