package com.doneit.ascend.domain.entity.dto

import com.doneit.ascend.domain.entity.AnswerEntity

data class AnswersDTO(
    val community: String,
    val answers: List<AnswerEntity>
)