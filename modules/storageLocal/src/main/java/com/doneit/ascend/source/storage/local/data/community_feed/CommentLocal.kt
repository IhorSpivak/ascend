package com.doneit.ascend.source.storage.local.data.community_feed

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.doneit.ascend.source.storage.local.data.chat.BlockedUserLocal

@Entity(
    tableName = "comment"
)
data class CommentLocal(
    @PrimaryKey
    val id: Long,
    val createdAt: Long,
    val isPostOwner: Boolean,
    val postCommentsCount: Int,
    val text: String,
    @Embedded(prefix = "user") val user: BlockedUserLocal
)