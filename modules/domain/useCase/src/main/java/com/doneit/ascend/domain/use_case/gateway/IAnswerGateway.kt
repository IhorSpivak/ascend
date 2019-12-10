package com.doneit.ascend.domain.use_case.gateway

import com.doneit.ascend.domain.entity.AnswerEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity

interface IAnswerGateway {
    suspend fun createAnswers(answers: List<AnswerEntity>): ResponseEntity<Unit, List<String>>
}