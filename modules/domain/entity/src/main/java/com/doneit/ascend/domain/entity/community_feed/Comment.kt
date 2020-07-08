package com.doneit.ascend.domain.entity.community_feed

import com.doneit.ascend.domain.entity.chats.BlockedUserEntity
import java.util.*

data class Comment(
    val id: Long,
    val createdAt: Date,
    val isPostOwner: Boolean,
    val postCommentsCount: Int,
    val text: String,
    val user: BlockedUserEntity
)