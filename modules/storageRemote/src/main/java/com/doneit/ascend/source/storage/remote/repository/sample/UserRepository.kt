package com.doneit.ascend.source.storage.remote.repository.sample

import com.vrgsoft.core.remote.BaseRepository
import com.doneit.ascend.source.storage.remote.api.UserApi
import com.doneit.ascend.source.storage.remote.data.request.LoginRequest
import com.doneit.ascend.source.storage.remote.data.request.RegistrationRequest
import com.doneit.ascend.source.storage.remote.data.response.LoginResponse
import com.vrgsoft.core.remote.result.BaseResult

internal class UserRepository(
    private val api: UserApi
) : BaseRepository(), IUserRepository {

    override suspend fun login(request: LoginRequest): BaseResult<LoginResponse> {
        return execute { api.login(request) }.transformIsSuccess { it }
    }
}