package com.doneit.ascend.source.storage.local.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OwnerLocal(
    @PrimaryKey val id: Long,
    val fullName: String,
    @Embedded(prefix = "owner_img") val image: ImageLocal?,
    val rating: Float,
    val followed: Boolean,
    val location: String,
    val connected: Boolean
)