package com.doneit.ascend.source.storage.local.data.chat

import androidx.room.Embedded
import androidx.room.Relation
import com.doneit.ascend.source.storage.local.data.community_feed.PostLocal
import com.doneit.ascend.source.storage.local.data.community_feed.PostWithAttachments

data class MessageWithPost(
    @Embedded
    val messageLocal: MessageLocal,
    @Relation(entity= PostLocal::class, parentColumn = "postId", entityColumn = "id")
    val post: PostWithAttachments?
)