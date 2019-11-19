package com.doneit.ascend.source.storage.remote.data.request

import com.google.gson.annotations.SerializedName

data class LogInRequest(
    @SerializedName("phone_number") val number: String,
    @SerializedName("password") val password: String
)