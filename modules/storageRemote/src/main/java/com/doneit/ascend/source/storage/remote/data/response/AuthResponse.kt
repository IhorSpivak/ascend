package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("session_token") val token: String,
    @SerializedName("current_user") val user: UserResponse,
    @SerializedName("errors") val errors: List<String>?
)