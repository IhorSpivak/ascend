package com.doneit.ascend.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AnswerOptionEntity(
    val id: Long,
    val title: String
) : Parcelable