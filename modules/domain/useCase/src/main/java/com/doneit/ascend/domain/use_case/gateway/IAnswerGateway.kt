package com.doneit.ascend.domain.use_case.gateway

import com.doneit.ascend.domain.entity.AnswersEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity

interface IAnswerGateway {
    suspend fun createAnswers(answers: AnswersEntity): ResponseEntity<Unit, List<String>>
}