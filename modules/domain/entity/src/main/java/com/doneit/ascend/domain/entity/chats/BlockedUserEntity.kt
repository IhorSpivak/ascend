package com.doneit.ascend.domain.entity.chats

import android.os.Parcelable
import com.doneit.ascend.domain.entity.ImageEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BlockedUserEntity (
    val id: Long,
    val fullName: String,
    val image: ImageEntity?
): Parcelable