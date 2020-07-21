package com.doneit.ascend.source.storage.local.data.community_feed

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "post_attachment",
    foreignKeys = [
        ForeignKey(
            entity = PostLocal::class,
            parentColumns = ["id"],
            childColumns = ["post_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            value = ["post_id"],
            unique = false
        )
    ]
)
data class PostAttachmentLocal(
    @PrimaryKey
    val id: Long,
    val contentType: Int,
    val post_id: Long,
    val url: String,
    val width: Int,
    val height: Int,
    val thumbnail: String
)