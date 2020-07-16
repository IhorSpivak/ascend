package com.doneit.ascend.source.storage.local.data.chat

import androidx.room.Embedded
import androidx.room.Relation

data class ChatWithLastMessage(
    @Embedded
    val chatLocal: ChatLocal,
    @Relation(entity= MessageLocal::class, parentColumn = "lastMessageId", entityColumn = "id")
    val lastMessage: MessageWithPost?
)