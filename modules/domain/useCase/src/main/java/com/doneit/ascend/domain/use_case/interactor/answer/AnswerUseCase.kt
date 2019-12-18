package com.doneit.ascend.domain.use_case.interactor.answer

import com.doneit.ascend.domain.entity.AnswersEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity

interface AnswerUseCase {
    suspend fun createAnswers(answers: AnswersEntity): ResponseEntity<Unit, List<String>>
}