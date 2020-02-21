package com.doneit.ascend.domain.use_case.gateway

import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.AnswersDTO

interface IAnswerGateway {
    suspend fun createAnswers(answers: AnswersDTO): ResponseEntity<Unit, List<String>>
}