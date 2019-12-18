package com.doneit.ascend.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CommunityQuestionEntity(
    val title: String,
    val answerOptions: List<String>
) : Parcelable