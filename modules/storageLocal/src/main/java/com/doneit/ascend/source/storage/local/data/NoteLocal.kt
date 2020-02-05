package com.doneit.ascend.source.storage.local.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteLocal(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val content: String,
    val updatedAt: String
)