package com.doneit.ascend.domain.use_case.interactor.user

import com.doneit.ascend.domain.entity.LoginUserModel

interface UserUseCase {
    suspend fun login(registerModel: LoginUserModel): Boolean
}