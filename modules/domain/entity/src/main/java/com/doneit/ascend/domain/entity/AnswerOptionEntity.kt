package com.doneit.ascend.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AnswerOptionEntity(
    val id: Long,
    val title: String,
    val position: Long,
    val createdAt: String,
    val updatedAt: String
) : Parcelable