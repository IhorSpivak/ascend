package com.vrgsoft.carButler.source.storage.remote.data.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("phone_number") val number: String,
    @SerializedName("password") val password: String
)