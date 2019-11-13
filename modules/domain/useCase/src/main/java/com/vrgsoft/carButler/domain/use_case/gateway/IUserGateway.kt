package com.vrgsoft.carButler.domain.use_case.gateway

import androidx.lifecycle.LiveData
import com.vrgsoft.carButler.domain.entity.LoginUserModel
import com.vrgsoft.carButler.domain.entity.User

interface IUserGateway {
    fun login(registerModel: LoginUserModel): LiveData<User>
}