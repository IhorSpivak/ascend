package com.doneit.ascend.domain.entity

data class AnswersEntity(
    val community: String,
    val answers: List<AnswerEntity>
)