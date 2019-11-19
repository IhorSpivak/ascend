package com.doneit.ascend.source.storage.remote.repository

import com.doneit.ascend.source.storage.remote.api.UserApi
import com.doneit.ascend.source.storage.remote.data.request.LoginRequest
import com.doneit.ascend.source.storage.remote.data.response.LoginResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse
import com.doneit.ascend.source.storage.remote.repository.base.BaseRepository
import com.google.gson.Gson

internal class UserRepository(
    gson: Gson,
    private val api: UserApi
) : BaseRepository(gson), IUserRepository {

    override suspend fun login(request: LoginRequest): RemoteResponse<LoginResponse, ErrorsListResponse> {
        return execute ( { api.login(request) }, ErrorsListResponse::class.java)
    }
}