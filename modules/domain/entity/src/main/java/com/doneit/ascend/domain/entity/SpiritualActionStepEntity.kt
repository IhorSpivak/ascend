package com.doneit.ascend.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SpiritualActionStepEntity(
    val id: Int,
    val name: String?,
    val isCompleted: Boolean
) : Parcelable