package com.doneit.ascend.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MessageAttachment(
    val name: String,
    val size: String,
    val type: AttachmentType,
    val url: String
) : Parcelable