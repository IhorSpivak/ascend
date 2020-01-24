package com.doneit.ascend.domain.use_case.interactor.answer

import com.doneit.ascend.domain.entity.dto.AnswersModel
import com.doneit.ascend.domain.entity.common.ResponseEntity

interface AnswerUseCase {
    suspend fun createAnswers(answers: AnswersModel): ResponseEntity<Unit, List<String>>
}