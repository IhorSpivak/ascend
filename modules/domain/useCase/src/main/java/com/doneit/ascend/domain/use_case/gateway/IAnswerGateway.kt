package com.doneit.ascend.domain.use_case.gateway

import com.doneit.ascend.domain.entity.dto.AnswersModel
import com.doneit.ascend.domain.entity.common.ResponseEntity

interface IAnswerGateway {
    suspend fun createAnswers(answers: AnswersModel): ResponseEntity<Unit, List<String>>
}