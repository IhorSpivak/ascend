package com.doneit.ascend.domain.use_case.interactor.user

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.use_case.gateway.IUserGateway

internal class UserInteractor(
    private val userGateway: IUserGateway
) : UserUseCase {
    override fun login(registerModel: com.doneit.ascend.domain.entity.LoginUserModel): LiveData<com.doneit.ascend.domain.entity.User> {
        return userGateway.login(registerModel)
    }
}