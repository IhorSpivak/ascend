package com.doneit.ascend.source.storage.local.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attachments")
data class AttachmentLocal(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "full_name") val fileName: String,
    @ColumnInfo(name = "file_size") val fileSize: Long,
    val link: String,
    @ColumnInfo(name = "group_id") val groupId: Long,
    @ColumnInfo(name = "user_id") val userId: Long,
    val privacy: Boolean,
    @ColumnInfo(name = "attachment_type") val attachmentType: String,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "updated_at") val updatedAt: String
)