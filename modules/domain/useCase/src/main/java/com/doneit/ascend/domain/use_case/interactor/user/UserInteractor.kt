package com.doneit.ascend.domain.use_case.interactor.user

import com.doneit.ascend.domain.entity.LoginUserModel
import com.doneit.ascend.domain.entity.RequestEntity
import com.doneit.ascend.domain.entity.User
import com.doneit.ascend.domain.use_case.gateway.IUserGateway

internal class UserInteractor(
    private val userGateway: IUserGateway
) : UserUseCase {
    override suspend fun login(registerModel: LoginUserModel): RequestEntity<User, List<String>> {
        return userGateway.login(registerModel)
    }
}