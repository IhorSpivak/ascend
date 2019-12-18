package com.doneit.ascend.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class QuestionListEntity(
    val questions: List<QuestionEntity>,
    val community: CommunityQuestionEntity?
) : Parcelable