package com.doneit.ascend.source.storage.remote.data.request

import com.google.gson.annotations.SerializedName

data class ChangeEmailRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)