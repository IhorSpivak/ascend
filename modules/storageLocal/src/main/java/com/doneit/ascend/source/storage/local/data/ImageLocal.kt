package com.doneit.ascend.source.storage.local.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ImageLocal(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val url: String?,
    @Embedded(prefix = "thumb") val thumbnail: ThumbnailLocal?
)

@Entity
data class ThumbnailLocal(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val url: String?
)