package com.doneit.ascend.source.storage.remote.data.response

import com.doneit.ascend.source.storage.remote.data.response.community_feed.PostResponse
import com.doneit.ascend.source.storage.remote.data.response.group.GroupResponse
import com.doneit.ascend.source.storage.remote.data.response.user.UserProfileResponse
import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("message")
    val message: String?,
    @SerializedName("edited")
    val edited: Boolean?,
    @SerializedName("message_type")
    val messageType: String?,
    @SerializedName("user_id")
    val userId: Long,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("status")
    val status: String?,
    @SerializedName("post")
    val post: PostResponse?,
    @SerializedName("attachment")
    val attachment: MessageAttachmentResponse?,
    @SerializedName("user")
    val sharedUser: UserProfileResponse?,
    @SerializedName("group")
    val sharedGroup: GroupResponse?
)