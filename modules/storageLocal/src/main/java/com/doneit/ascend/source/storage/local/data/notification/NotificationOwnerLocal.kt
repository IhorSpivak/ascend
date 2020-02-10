package com.doneit.ascend.source.storage.local.data.notification

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NotificationOwnerLocal(
    @PrimaryKey val id: Long?,
    val fullName: String?,
    val displayName: String?
)