package com.doneit.ascend.domain.use_case.gateway

import com.doneit.ascend.domain.entity.dto.AnswersDTO
import com.doneit.ascend.domain.entity.common.ResponseEntity

interface IAnswerGateway {
    suspend fun createAnswers(answers: AnswersDTO): ResponseEntity<Unit, List<String>>
}