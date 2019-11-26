package com.doneit.ascend.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuestionEntity(
    val id: Long,
    val title: String,
    val type: String,
    val createdAt: String,
    val updatedAt: String,
    val options: List<AnswerOptionEntity>?
) : Parcelable