package com.doneit.ascend.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AttendeeEntity(
    val id: Int,
    val fullName: String,
    val email: String,
    val imageUrl: String?
) : Parcelable