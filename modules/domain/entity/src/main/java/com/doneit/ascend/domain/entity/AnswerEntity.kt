package com.doneit.ascend.domain.entity

data class AnswerEntity(
    val questionId: Long,
    val answer: String,
    val answerOptionId: Long
)