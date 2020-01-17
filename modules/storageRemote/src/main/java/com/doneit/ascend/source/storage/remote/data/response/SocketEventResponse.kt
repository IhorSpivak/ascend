package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class SocketEventResponse(
    @SerializedName("event") val event: String,
    @SerializedName("user_id") val userId: Long,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("image") val image: ImageResponse
)