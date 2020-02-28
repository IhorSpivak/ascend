package com.doneit.ascend.domain.entity.ascension.goal

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class GoalDurationEntity(
    val type: GoalDurationType,
    val value: Int
) : Parcelable