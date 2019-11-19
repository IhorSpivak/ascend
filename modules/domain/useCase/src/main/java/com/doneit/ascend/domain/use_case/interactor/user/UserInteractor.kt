package com.doneit.ascend.domain.use_case.interactor.user

import com.doneit.ascend.domain.entity.AuthEntity
import com.doneit.ascend.domain.entity.LogInUserModel
import com.doneit.ascend.domain.entity.SignUpModel
import com.doneit.ascend.domain.entity.common.RequestEntity
import com.doneit.ascend.domain.use_case.gateway.IUserGateway

internal class UserInteractor(
    private val userGateway: IUserGateway
) : UserUseCase {
    override suspend fun signIn(logInModel: LogInUserModel): RequestEntity<AuthEntity, List<String>> {
        return userGateway.signIn(logInModel)
    }

    override suspend fun signUp(registerModel: SignUpModel): RequestEntity<AuthEntity, List<String>> {
        return userGateway.signUp(registerModel)
    }
}