package com.doneit.ascend.providers

import com.doneit.ascend.networking.API
import com.doneit.ascend.ui.signup.Registration
import com.google.gson.annotations.SerializedName
import javax.inject.Inject
import javax.inject.Singleton

class LoginRequest(
    @SerializedName("phone_number") val number: String,
    @SerializedName("password") val password: String
)

class User(
    @SerializedName("full_name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone_number") val phone: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)

class LoginResponse(
    @SerializedName("session_token") val token: String,
    @SerializedName("current_user") val user: User
)

@Singleton
open class SignInUpProvider @Inject constructor(
    var apiProvider: API,
    val storageProvider: StorageProvider
) {

    fun logIn(phone: String, password: String) = apiProvider
        .login(LoginRequest(phone, password))
        .doOnNext {
            storageProvider.token = it.token
        }

    fun registration(registration: Registration) = apiProvider.registration(registration)

}
