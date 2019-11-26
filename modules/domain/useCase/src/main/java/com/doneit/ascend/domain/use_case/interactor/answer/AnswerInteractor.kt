package com.doneit.ascend.domain.use_case.interactor.answer

import com.doneit.ascend.domain.entity.AnswerEntity
import com.doneit.ascend.domain.entity.common.RequestEntity
import com.doneit.ascend.domain.use_case.gateway.IAnswerGateway

internal class AnswerInteractor(
    private val answerGateway: IAnswerGateway
) : AnswerUseCase {

    override suspend fun createAnswers(
        sessionToken: String,
        answers: List<AnswerEntity>
    ): RequestEntity<Unit, List<String>> {
        return answerGateway.createAnswers(sessionToken, answers)
    }
}