package com.doneit.ascend.domain.entity.chats

import android.os.Parcelable
import com.doneit.ascend.domain.entity.ImageEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MemberEntity(
    val id: Long,
    var fullName: String,
    var online: Boolean,
    var leaved: Boolean,
    val removed: Boolean,
    val image: ImageEntity?
) : Parcelable