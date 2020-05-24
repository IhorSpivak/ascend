package com.doneit.ascend.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WebinarQuestionEntity(
    val id: Long,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
    val userId: Long,
    val fullName: String,
    val image: ImageEntity
) : Parcelable