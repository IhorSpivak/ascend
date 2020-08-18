package com.doneit.ascend.source.storage.local.data.notification

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification")
data class NotificationLocal(
    @PrimaryKey val id: Long?,
    val notificationType: String,
    val groupId: Long?,
    val groupName: String?,
    val entity_name: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val title: String,
    @Embedded(prefix = "owner") val owner: NotificationOwnerLocal?,
    @ColumnInfo(name = "is_read") val isRead: Boolean = false
)