package com.doneit.ascend.source.storage.remote.repository.sample

import com.doneit.ascend.source.storage.remote.data.request.LoginRequest
import com.doneit.ascend.source.storage.remote.data.response.LoginResponse
import com.vrgsoft.core.remote.result.BaseResult

interface IUserRepository {
    suspend fun login(request: LoginRequest): BaseResult<LoginResponse>
}