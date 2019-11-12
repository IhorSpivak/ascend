package com.doneit.ascend.networking

import com.doneit.ascend.providers.LoginRequest
import com.doneit.ascend.providers.LoginResponse
import com.doneit.ascend.providers.User
import com.doneit.ascend.ui.login.Login
import com.doneit.ascend.ui.signup.ConfirmPhone
import com.doneit.ascend.ui.signup.Registration
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface API {

    @POST("sessions")
    fun login(@Body request: LoginRequest): Observable<LoginResponse>

    @DELETE("sessions/logout")
    fun logOut()

    @POST("users")
    fun registration(@Body request: Registration): Observable<User>

    @POST("users/confirm")
    fun confirmPhone(@Body request: ConfirmPhone)
}
