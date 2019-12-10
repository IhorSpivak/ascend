package com.doneit.ascend.domain.use_case.interactor.answer

import com.doneit.ascend.domain.entity.AnswerEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.use_case.gateway.IAnswerGateway

internal class AnswerInteractor(
    private val answerGateway: IAnswerGateway
) : AnswerUseCase {

    override suspend fun createAnswers(
        answers: List<AnswerEntity>
    ): ResponseEntity<Unit, List<String>> {
        return answerGateway.createAnswers(answers)
    }
}