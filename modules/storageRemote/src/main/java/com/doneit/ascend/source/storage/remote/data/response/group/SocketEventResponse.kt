package com.doneit.ascend.source.storage.remote.data.response.group

import com.doneit.ascend.source.storage.remote.data.response.ImageResponse
import com.google.gson.annotations.SerializedName

data class SocketEventResponse(
    @SerializedName("identifier") val identifier: String,
    @SerializedName("message") val message: SocketEventMessage?
)

data class SocketEventMessage(
    @SerializedName("event") val event: String?,
    @SerializedName("user_id") val userId: Long,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("image") val image: ImageResponse?
)