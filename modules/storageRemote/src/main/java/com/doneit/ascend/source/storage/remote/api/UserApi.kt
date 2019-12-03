package com.doneit.ascend.source.storage.remote.api

import com.doneit.ascend.source.storage.remote.data.request.*
import com.doneit.ascend.source.storage.remote.data.response.AuthResponse
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface UserApi {

    @POST("sessions")
    fun signIn(@Body request: LogInRequest): Deferred<Response<AuthResponse>>

    @POST("sessions/social_login")
    fun socialSignInAsync(@Body request: SocialLoginRequest): Deferred<Response<AuthResponse>>

    @DELETE("sessions/logout")
    fun logOut()

    @POST("users")
    fun signUp(@Body request: SignUpRequest): Deferred<Response<AuthResponse>>

    @POST("users/validation")
    fun signUpValidation(@Body request: SignUpRequest): Deferred<Response<OKResponse>>

    @POST("users/get_code")
    fun getConfirmationCode(@Body request: PhoneRequest): Deferred<Response<OKResponse>>

    @POST("users/forgot_password")
    fun forgotPassword(@Body request: PhoneRequest): Deferred<Response<OKResponse>>

    @POST("users/reset_password")
    fun resetPassword(@Body request: ResetPasswordRequest): Deferred<Response<OKResponse>>
}