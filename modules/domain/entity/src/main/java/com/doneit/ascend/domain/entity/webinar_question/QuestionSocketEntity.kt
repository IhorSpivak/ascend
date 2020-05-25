package com.doneit.ascend.domain.entity.webinar_question

import com.doneit.ascend.domain.entity.ImageEntity


data class QuestionSocketEntity(
    val id: Long,
    val question: String,
    val userId: Long,
    val createdAt: String?,
    val updatedAt: String?,
    val fullName: String,
    val image: ImageEntity?,
    val event: QuestionSocketEvent

)