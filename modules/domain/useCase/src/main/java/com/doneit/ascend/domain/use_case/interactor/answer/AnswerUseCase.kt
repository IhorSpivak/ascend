package com.doneit.ascend.domain.use_case.interactor.answer

import com.doneit.ascend.domain.entity.AnswerEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity

interface AnswerUseCase {
    suspend fun createAnswers(answers: List<AnswerEntity>): ResponseEntity<Unit, List<String>>
}