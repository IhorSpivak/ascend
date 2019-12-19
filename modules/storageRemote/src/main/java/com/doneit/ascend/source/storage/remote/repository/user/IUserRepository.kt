package com.doneit.ascend.source.storage.remote.repository.user

import com.doneit.ascend.source.storage.remote.data.request.*
import com.doneit.ascend.source.storage.remote.data.response.AuthResponse
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse

interface IUserRepository {

    suspend fun signIn(request: LogInRequest): RemoteResponse<AuthResponse, ErrorsListResponse>

    suspend fun socialSignIn(request: SocialLoginRequest): RemoteResponse<AuthResponse, ErrorsListResponse>

    suspend fun signOut(): RemoteResponse<OKResponse, ErrorsListResponse>

    suspend fun signUp(request: SignUpRequest): RemoteResponse<AuthResponse, ErrorsListResponse>

    suspend fun deleteAccount(): RemoteResponse<OKResponse, ErrorsListResponse>

    suspend fun signUpValidation(request: SignUpRequest): RemoteResponse<OKResponse, ErrorsListResponse>

    suspend fun getConfirmationCode(request: PhoneRequest): RemoteResponse<OKResponse, ErrorsListResponse>

    suspend fun forgotPassword(request: PhoneRequest): RemoteResponse<OKResponse, ErrorsListResponse>

    suspend fun resetPassword(request: ResetPasswordRequest): RemoteResponse<OKResponse, ErrorsListResponse>

    suspend fun report(content: String, id: Long): RemoteResponse<OKResponse, ErrorsListResponse>
}