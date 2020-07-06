package com.doneit.ascend.source.storage.local.data.community_feed

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.doneit.ascend.source.storage.local.data.OwnerLocal

@Entity(
    tableName = "post"
)
data class PostLocal(
    @PrimaryKey
    val id: Long,
    val commentsCount: Int,
    val createdAt: Long,
    val description: String,
    val isLikedMe: Boolean,
    val isOwner: Boolean,
    val likesCount: Int,
    @Embedded(prefix = "owner")
    val owner: OwnerLocal,
    val updatedAt: Long
)