package com.doneit.ascend.source.storage.remote.data.request

import com.google.gson.annotations.SerializedName

data class ChangePhoneRequest(
    @SerializedName("password") val password: String,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("code") val code: String
)