package com.doneit.ascend.domain.entity.webinar_question

import android.os.Parcelable
import com.doneit.ascend.domain.entity.ImageEntity
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class WebinarQuestionEntity(
    val id: Long,
    val content: String,
    val createdAt: Date?,
    val updatedAt: Date?,
    val userId: Long,
    val fullName: String,
    val image: ImageEntity?
) : Parcelable