package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class ChatResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("members_count")
    val membersCount: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    val online: Boolean,
    val blocked: Boolean,
    @SerializedName("unread_message_count")
    val unreadMessageCount: Int,
    @SerializedName("user_id")
    val chatOwnerId: Long,
    @SerializedName("image")
    val image: ImageResponse?,
    @SerializedName("last_message")
    val lastMessage: MessageResponse?,
    @SerializedName("type")
    val chatType: String,
    @SerializedName("private")
    val isPrivate: Boolean,
    val subscribed: Boolean,
    @SerializedName("owner")
    val owner: OwnerResponse?
)