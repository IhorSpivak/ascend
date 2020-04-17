package com.doneit.ascend.domain.entity.chats

import java.util.*

data class MessageEntity(
    val id: Long,
    val message: String,
    val edited: Boolean = false,
    val userId: Long,
    val createdAt: Date?,
    val updatedAt: Date?,
    val status: MessageStatus
)
