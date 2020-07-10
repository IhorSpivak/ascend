package com.doneit.ascend.domain.entity.community_feed

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Attachment(
    val id: String,
    val contentType: ContentType,
    val url: String
) : Parcelable