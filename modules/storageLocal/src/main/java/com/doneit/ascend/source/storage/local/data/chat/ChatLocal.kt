package com.doneit.ascend.source.storage.local.data.chat

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.doneit.ascend.source.storage.local.data.ImageLocal
import com.doneit.ascend.source.storage.local.data.OwnerLocal
import com.doneit.ascend.source.storage.local.data.converters.MembersConverter

@Entity(tableName = "chat")
data class ChatLocal(
    @PrimaryKey val id: Long,
    val membersCount: Int,
    val createdAt: String?,
    val updatedAt: String?,
    val online: Boolean,
    val blocked: Boolean,
    val unreadMessageCount: Int,
    val chatOwnerId: Long,
    val title: String,
    @Embedded(prefix = "img") val image: ImageLocal?,
    val lastMessageId: Long?,
    @TypeConverters(MembersConverter::class)
    val members: List<MemberLocal>?,
    val chatType: String,
    val isPrivate: Boolean,
    val subscribed: Boolean,
    @Embedded(prefix = "owner") val owner: OwnerLocal?
)