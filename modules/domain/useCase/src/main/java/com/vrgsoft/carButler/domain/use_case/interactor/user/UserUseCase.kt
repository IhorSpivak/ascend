package com.vrgsoft.carButler.domain.use_case.interactor.user

import androidx.lifecycle.LiveData
import com.vrgsoft.carButler.domain.entity.LoginUserModel
import com.vrgsoft.carButler.domain.entity.User

interface UserUseCase {
    fun login(registerModel: LoginUserModel): LiveData<User>
}