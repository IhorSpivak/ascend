package com.doneit.ascend.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TimeCommitmentEntity(
    val type: TimeCommitmentType,
    val value: Int
): Parcelable