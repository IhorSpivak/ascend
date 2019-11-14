package com.doneit.ascend.source.storage.remote.api

import com.doneit.ascend.source.storage.remote.data.request.ConfirmPhoneRequest
import com.doneit.ascend.source.storage.remote.data.request.LoginRequest
import com.doneit.ascend.source.storage.remote.data.request.RegistrationRequest
import com.doneit.ascend.source.storage.remote.data.response.LoginResponse
import com.doneit.ascend.source.storage.remote.data.response.UserResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface UserApi {

    @POST("sessions")
    fun login(@Body request: LoginRequest): Deferred<Response<LoginResponse>>

    @DELETE("sessions/logout")
    fun logOut()

    @POST("users")
    fun registration(@Body request: RegistrationRequest): Deferred<Response<UserResponse>>

    @POST("users/confirm")
    fun confirmPhone(@Body request: ConfirmPhoneRequest)
}