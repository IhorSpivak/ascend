package com.doneit.ascend.domain.entity.chats

import android.os.Parcelable
import com.doneit.ascend.domain.entity.ImageEntity
import com.doneit.ascend.domain.entity.OwnerEntity
import com.doneit.ascend.domain.entity.dto.ChatType
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ChatEntity(
    val id: Long,
    var title: String,
    var description: String?,
    var membersCount: Int,
    val createdAt: Date?,
    var updatedAt: Date?,
    var online: Boolean,
    val blocked: Boolean,
    val unreadMessageCount: Int,
    val chatOwnerId: Long,
    val image: ImageEntity?,
    var lastMessage: MessageEntity?,
    var members: List<MemberEntity>,
    val chatType: ChatType,
    val isPrivate: Boolean,
    val isSubscribed: Boolean,
    val owner: OwnerEntity?
) : Parcelable {

    fun inheritFrom(chat: ChatEntity) {
        title = chat.title
        description = chat.description
        membersCount = chat.membersCount
        updatedAt = chat.updatedAt
        lastMessage = chat.lastMessage
        members = chat.members
    }
}