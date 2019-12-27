package com.doneit.ascend.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageEntity(
    val url: String?,
    val thumbnail: ThumbnailEntity?
): Parcelable