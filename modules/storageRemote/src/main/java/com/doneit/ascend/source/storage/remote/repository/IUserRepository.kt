package com.doneit.ascend.source.storage.remote.repository

import com.doneit.ascend.source.storage.remote.data.request.LoginRequest
import com.doneit.ascend.source.storage.remote.data.response.LoginResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse

interface IUserRepository {
    suspend fun login(request: LoginRequest): RemoteResponse<LoginResponse, ErrorsListResponse>
}