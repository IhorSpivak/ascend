package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class OKResponse(
    @SerializedName("message") val message: String
)