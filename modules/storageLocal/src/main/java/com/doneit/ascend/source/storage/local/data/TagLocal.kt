package com.doneit.ascend.source.storage.local.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TagLocal(
    @PrimaryKey
    val id: Int,
    val tag: String
)