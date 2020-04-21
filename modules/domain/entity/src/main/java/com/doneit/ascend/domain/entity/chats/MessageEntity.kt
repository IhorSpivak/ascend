package com.doneit.ascend.domain.entity.chats

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class MessageEntity(
    val id: Long,
    val message: String,
    val edited: Boolean = false,
    val userId: Long,
    val createdAt: Date?,
    val updatedAt: Date?,
    val status: MessageStatus
) : Parcelable
