package com.doneit.ascend.source.storage.local.data.chat

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.doneit.ascend.source.storage.local.data.ImageLocal

@Entity(tableName = "blocked_users")
data class BlockedUserLocal (
    @PrimaryKey val id: Long,
    val fullName: String,
    @Embedded(prefix = "img") val image: ImageLocal?
)