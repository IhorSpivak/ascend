package com.doneit.ascend.domain.entity.community_feed

data class CommunityFeedSocketEntity(
    val postId: Long,
    val commentsCount: Int,
    val likesCount: Int,
    val event: CommunityFeedSocketEvent
)