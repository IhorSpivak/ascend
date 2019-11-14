package com.doneit.ascend.domain.use_case.gateway

import androidx.lifecycle.LiveData

interface IUserGateway {
    fun login(registerModel: com.doneit.ascend.domain.entity.LoginUserModel): LiveData<com.doneit.ascend.domain.entity.User>
}