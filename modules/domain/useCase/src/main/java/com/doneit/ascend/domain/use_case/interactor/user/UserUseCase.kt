package com.doneit.ascend.domain.use_case.interactor.user

import com.doneit.ascend.domain.entity.LoginUserModel
import com.doneit.ascend.domain.entity.RequestEntity
import com.doneit.ascend.domain.entity.User

interface UserUseCase {
    suspend fun login(registerModel: LoginUserModel): RequestEntity<User, List<String>>
}