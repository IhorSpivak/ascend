package com.doneit.ascend.source.storage.remote.data.request

import com.google.gson.annotations.SerializedName

data class ConfirmPhoneRequest(
    @SerializedName("Session-Token") val token: String,
    @SerializedName("code") val code: String
)