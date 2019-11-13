package com.vrgsoft.carButler.domain.use_case.interactor.user

import androidx.lifecycle.LiveData
import com.vrgsoft.carButler.domain.entity.LoginUserModel
import com.vrgsoft.carButler.domain.entity.User
import com.vrgsoft.carButler.domain.use_case.gateway.IUserGateway

internal class UserInteractor(
    private val userGateway: IUserGateway
) : UserUseCase {
    override fun login(registerModel: LoginUserModel): LiveData<User> {
        return userGateway.login(registerModel)
    }
}