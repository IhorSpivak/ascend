package com.doneit.ascend.domain.use_case.interactor.answer

import com.doneit.ascend.domain.entity.AnswerEntity
import com.doneit.ascend.domain.entity.common.RequestEntity

interface AnswerUseCase {
    suspend fun createAnswers(answers: List<AnswerEntity>): RequestEntity<Unit, List<String>>
}