package com.doneit.ascend.domain.entity.community_feed

import com.doneit.ascend.domain.entity.OwnerEntity
import java.util.*

data class Post(
    val id: Long,
    val attachments: List<Attachment>,
    val commentsCount: Int,
    val createdAt: Date,
    val description: String,
    val isLikedMe: Boolean,
    val isOwner: Boolean,
    val likesCount: Int,
    val owner: OwnerEntity,
    val updatedAt: Date
)