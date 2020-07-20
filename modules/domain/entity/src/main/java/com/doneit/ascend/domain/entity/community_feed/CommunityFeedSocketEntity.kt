package com.doneit.ascend.domain.entity.community_feed

data class CommunityFeedSocketEntity(
    val postId: Long,
    val commentsCount: Int,
    val event: CommunityFeedSocketEvent
)