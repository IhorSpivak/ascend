package com.doneit.ascend.domain.use_case.interactor.user

import androidx.lifecycle.LiveData

interface UserUseCase {
    fun login(registerModel: com.doneit.ascend.domain.entity.LoginUserModel): LiveData<com.doneit.ascend.domain.entity.User>
}