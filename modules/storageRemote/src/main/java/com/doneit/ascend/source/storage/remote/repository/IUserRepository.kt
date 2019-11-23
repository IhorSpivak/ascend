package com.doneit.ascend.source.storage.remote.repository

import com.doneit.ascend.source.storage.remote.data.request.PhoneRequest
import com.doneit.ascend.source.storage.remote.data.request.LogInRequest
import com.doneit.ascend.source.storage.remote.data.request.SignUpRequest
import com.doneit.ascend.source.storage.remote.data.response.AuthResponse
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse

interface IUserRepository {

    suspend fun signIn(request: LogInRequest): RemoteResponse<AuthResponse, ErrorsListResponse>

    suspend fun signUp(request: SignUpRequest): RemoteResponse<AuthResponse, ErrorsListResponse>

    suspend fun getConfirmationCode(request: PhoneRequest): RemoteResponse<OKResponse, ErrorsListResponse>

    suspend fun forgotPassword(request: PhoneRequest): RemoteResponse<OKResponse, ErrorsListResponse>
}