package com.doneit.ascend.source.storage.local.data.chat

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.doneit.ascend.source.storage.local.data.GroupLocal
import com.doneit.ascend.source.storage.local.data.UserLocal

@Entity(tableName = "messages")
data class MessageLocal(
    @PrimaryKey val id: Long,
    val message: String,
    val userId: Long,
    val edited: Boolean,
    val type: String,
    val createdAt: Long?,
    val updatedAt: Long?,
    val status: String,
    val chatId: Long,
    val postId: Long?,
    @Embedded(prefix = "attachment")
    val attachment: MessageAttachmentLocal?,
    @Embedded(prefix = "shared_user")
    val sharedUser: UserLocal?,
    @Embedded(prefix = "shared_group")
    val sharedGroup: GroupLocal?
)