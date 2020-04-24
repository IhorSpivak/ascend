package com.doneit.ascend.domain.entity.chats

import android.os.Parcelable
import com.doneit.ascend.domain.entity.ImageEntity
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ChatEntity(
    val id: Long,
    var title: String,
    val membersCount: Int,
    val createdAt: Date?,
    val updatedAt: Date?,
    val online: Boolean,
    val blocked: Boolean,
    val unreadMessageCount: Int,
    val chatOwnerId: Long,
    val image: ImageEntity?,
    val lastMessage: MessageEntity?,
    var members: List<MemberEntity>?
): Parcelable