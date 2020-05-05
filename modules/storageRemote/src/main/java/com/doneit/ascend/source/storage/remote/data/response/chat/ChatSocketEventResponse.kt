package com.doneit.ascend.source.storage.remote.data.response.chat

import com.google.gson.annotations.SerializedName

data class ChatSocketEventResponse(
    @SerializedName("identifier") val identifier: String,
    @SerializedName("message") val message: ChatSocketEventMessage?
)

data class ChatSocketEventMessage(
    @SerializedName("id") val id: Long,
    @SerializedName("message") val message: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("message_type") val messageType: String?,
    @SerializedName("edited") val edited: Boolean?,
    @SerializedName("user_id") val userId: Long?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("event") val event: String?
)