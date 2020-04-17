package com.doneit.ascend.source.storage.local.data.chat

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.doneit.ascend.source.storage.local.data.ImageLocal

@Entity(tableName = "chat")
data class ChatLocal(
    @PrimaryKey val id: Long,
    val membersCount: Int,
    val createdAt: String?,
    val updatedAt: String?,
    val online: Boolean,
    val unreadMessageCount: Int,
    val chatOwnerId: Long,
    val title: String,
    @Embedded(prefix = "img") val image: ImageLocal?,
    @Embedded(prefix = "message") val lastMessage: MessageLocal?
)