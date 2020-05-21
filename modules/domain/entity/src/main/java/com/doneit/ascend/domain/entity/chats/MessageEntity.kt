package com.doneit.ascend.domain.entity.chats

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class MessageEntity(
    val id: Long,
    val message: String,
    val edited: Boolean = false,
    val type: MessageType,
    val userId: Long,
    val createdAt: Date?,
    val updatedAt: Date?,
    val status: MessageStatus,
    var isMarkAsReadSentToApprove: Boolean = false
) : Parcelable
