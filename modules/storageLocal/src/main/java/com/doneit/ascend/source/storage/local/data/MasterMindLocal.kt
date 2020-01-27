package com.doneit.ascend.source.storage.local.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "master_minds")
data class MasterMindLocal(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "full_name")val fullName: String?,
    val displayName: String?,
    val description: String?,
    val location: String?,
    val bio: String?,
    val groupsCount: Int?,
    val rating: Float,
    val followed: Boolean,
    val rated: Boolean,
    @Embedded(prefix = "img") val image: ImageLocal?,
    val allowRating: Boolean?,
    val myRating: Int?
)