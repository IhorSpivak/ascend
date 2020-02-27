package com.doneit.ascend.domain.entity.user

data class AnswerEntity(
    val questionId: Long,
    val answer: String?,
    val answerOptionId: Long?
)