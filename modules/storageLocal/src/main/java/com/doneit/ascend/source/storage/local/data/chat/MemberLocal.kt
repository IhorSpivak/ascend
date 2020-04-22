package com.doneit.ascend.source.storage.local.data.chat

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.doneit.ascend.source.storage.local.data.ImageLocal

@Entity(tableName = "members")
data class MemberLocal(
    @PrimaryKey val id: Long,
    val fullName: String,
    val online: Boolean,
    val leaved: Boolean,
    @Embedded(prefix = "img") val image: ImageLocal?
)