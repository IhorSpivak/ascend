package com.doneit.ascend.domain.use_case.interactor.user

import com.doneit.ascend.domain.entity.LoginUserModel
import com.doneit.ascend.domain.use_case.gateway.IUserGateway

internal class UserInteractor(
    private val userGateway: IUserGateway
) : UserUseCase {
    override suspend fun login(registerModel: LoginUserModel): Boolean {
        return userGateway.login(registerModel) != null
    }
}