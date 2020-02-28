package com.doneit.ascend.domain.entity.ascension.spiritual_action_step

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TimeCommitmentEntity(
    val type: TimeCommitmentType,
    val value: Int
): Parcelable