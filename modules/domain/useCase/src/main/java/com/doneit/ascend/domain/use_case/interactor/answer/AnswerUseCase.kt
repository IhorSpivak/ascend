package com.doneit.ascend.domain.use_case.interactor.answer

import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.AnswersDTO

interface AnswerUseCase {
    suspend fun createAnswers(answers: AnswersDTO): ResponseEntity<Unit, List<String>>
}