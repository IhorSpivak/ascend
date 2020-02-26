package com.doneit.ascend.source.storage.remote.data.request

import com.google.gson.annotations.SerializedName

data class CreateCardRequest(
    @SerializedName("fullName") val name: String,
    @SerializedName("token") val token: String
)