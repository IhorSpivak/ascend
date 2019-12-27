package com.doneit.ascend.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ThumbnailEntity(
    val url: String?
): Parcelable