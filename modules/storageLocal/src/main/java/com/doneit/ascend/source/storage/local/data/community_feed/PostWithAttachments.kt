package com.doneit.ascend.source.storage.local.data.community_feed

import androidx.room.Embedded
import androidx.room.Relation

data class PostWithAttachments(
    @Embedded
    val postLocal: PostLocal,
    @Relation(parentColumn = "id", entityColumn = "post_id")
    val attachments: List<PostAttachmentLocal>
)