package com.doneit.ascend.source.storage.remote.data.response.group

import com.doneit.ascend.source.storage.remote.data.response.ImageResponse
import com.google.gson.annotations.SerializedName

data class ParticipantResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("image") val image: ImageResponse?,
    @SerializedName("rise_a_hand") val isHandRisen: Boolean,
    @SerializedName("connected") val isConnected: Boolean,
    @SerializedName("visited") val isVisited: Boolean,
    @SerializedName("blocked") val isBlocked: Boolean,
    @SerializedName("speaker") val isSpeaker: Boolean
)