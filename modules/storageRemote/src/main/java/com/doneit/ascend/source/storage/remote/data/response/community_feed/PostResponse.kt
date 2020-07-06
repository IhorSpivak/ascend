package com.doneit.ascend.source.storage.remote.data.response.community_feed


import com.doneit.ascend.source.storage.remote.data.response.OwnerResponse
import com.google.gson.annotations.SerializedName

data class PostResponse(
    @SerializedName("attachments")
    val attachments: List<AttachmentResponse>? = null,
    @SerializedName("comments_count")
    val commentsCount: Int? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("is_liked_me")
    val isLikedMe: Boolean? = null,
    @SerializedName("is_owner")
    val isOwner: Boolean? = null,
    @SerializedName("likes_count")
    val likesCount: Int? = null,
    @SerializedName("owner")
    val owner: OwnerResponse? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null
)