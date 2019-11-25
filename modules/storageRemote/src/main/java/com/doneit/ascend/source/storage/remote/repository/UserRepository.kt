package com.doneit.ascend.source.storage.remote.repository

import com.doneit.ascend.source.storage.remote.api.UserApi
import com.doneit.ascend.source.storage.remote.data.request.PhoneRequest
import com.doneit.ascend.source.storage.remote.data.request.LogInRequest
import com.doneit.ascend.source.storage.remote.data.request.SignUpRequest
import com.doneit.ascend.source.storage.remote.data.response.AuthResponse
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse
import com.doneit.ascend.source.storage.remote.repository.base.BaseRepository
import com.google.gson.Gson

internal class UserRepository(
    gson: Gson,
    private val api: UserApi
) : BaseRepository(gson), IUserRepository {

    override suspend fun signIn(request: LogInRequest): RemoteResponse<AuthResponse, ErrorsListResponse> {
        return execute({ api.signIn(request) }, ErrorsListResponse::class.java)
    }

    override suspend fun signUp(request: SignUpRequest): RemoteResponse<AuthResponse, ErrorsListResponse> {
        return execute({ api.signUp(request) }, ErrorsListResponse::class.java)
    }

    override suspend fun getConfirmationCode(request: PhoneRequest): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({api.getConfirmationCode(request)}, ErrorsListResponse::class.java)
    }

    override suspend fun forgotPassword(request: PhoneRequest): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({api.forgotPassword(request)}, ErrorsListResponse::class.java)
    }
}