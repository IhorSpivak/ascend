package com.doneit.ascend.source.storage.local.data.chat

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageLocal(
    @PrimaryKey val id: Long,
    val message: String,
    val userId: Long,
    val edited: Boolean,
    val createdAt: Long?,
    val updatedAt: Long?,
    val status: String
)