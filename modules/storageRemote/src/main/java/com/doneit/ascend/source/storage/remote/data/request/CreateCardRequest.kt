package com.doneit.ascend.source.storage.remote.data.request

import com.google.gson.annotations.SerializedName

data class CreateCardRequest(
    @SerializedName("name") val name: String,
    @SerializedName("token") val token: String
)