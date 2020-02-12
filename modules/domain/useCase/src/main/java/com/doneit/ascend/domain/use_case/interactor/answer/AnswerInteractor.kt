package com.doneit.ascend.domain.use_case.interactor.answer

import com.doneit.ascend.domain.entity.dto.AnswersDTO
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.use_case.gateway.IAnswerGateway

internal class AnswerInteractor(
    private val answerGateway: IAnswerGateway
) : AnswerUseCase {

    override suspend fun createAnswers(
        answers: AnswersDTO
    ): ResponseEntity<Unit, List<String>> {
        return answerGateway.createAnswers(answers)
    }
}