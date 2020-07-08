package com.doneit.ascend.source.storage.remote.data.response


import com.google.gson.annotations.SerializedName

data class CommentResponse(
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("is_post_owner")
    val isPostOwner: Boolean? = null,
    @SerializedName("post_comments_count")
    val postCommentsCount: Int? = null,
    @SerializedName("text")
    val text: String? = null,
    @SerializedName("user")
    val user: BlockedUserResponse? = null
)