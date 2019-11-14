package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("session_token") val token: String,
    @SerializedName("current_user") val user: UserResponse
)