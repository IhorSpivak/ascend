package com.doneit.ascend.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OwnerEntity(
    val id: Long,
    val fullName: String,
    val image: ImageEntity,
    val rating: Float,
    val followed: Boolean
): Parcelable