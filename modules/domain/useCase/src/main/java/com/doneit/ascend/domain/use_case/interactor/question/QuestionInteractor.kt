package com.doneit.ascend.domain.use_case.interactor.question

import com.doneit.ascend.domain.entity.QuestionEntity
import com.doneit.ascend.domain.entity.common.RequestEntity
import com.doneit.ascend.domain.use_case.gateway.IQuestionGateway

internal class QuestionInteractor(
    private val questionGateway: IQuestionGateway
) : QuestionUseCase {
    override suspend fun getList(sessionToken: String): RequestEntity<List<QuestionEntity>, List<String>> {
        return questionGateway.getList(sessionToken)
    }

    override suspend fun insert(questions: List<QuestionEntity>) {
        questionGateway.insert(questions)
    }

    override suspend fun deleteAllQuestions() {
        questionGateway.deleteAllQuestions()
    }

    override suspend fun getQuestionsList(): List<QuestionEntity> {
        return questionGateway.getQuestionsList()
    }
}