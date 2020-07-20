package com.doneit.ascend.source.storage.remote.data.response.community_feed

import com.google.gson.annotations.SerializedName

data class CommunityFeedEventResponse(
    @SerializedName("identifier") val identifier: String,
    @SerializedName("message") val message: CommunityFeedEventMessage?
)

data class CommunityFeedEventMessage(
    @SerializedName("post_id") val postId: Long,
    @SerializedName("comments_count") val commentsCount: Int,
    @SerializedName("event") val event: String?
)