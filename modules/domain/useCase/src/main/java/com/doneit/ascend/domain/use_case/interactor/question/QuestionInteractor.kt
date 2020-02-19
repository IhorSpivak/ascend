package com.doneit.ascend.domain.use_case.interactor.question

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.QuestionListEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.use_case.gateway.IQuestionGateway

internal class QuestionInteractor(
    private val questionGateway: IQuestionGateway
) : QuestionUseCase {

    override suspend fun getList(): ResponseEntity<QuestionListEntity, List<String>> {
        return questionGateway.getList()
    }

    override fun getQuestionsList(): LiveData<QuestionListEntity?> {
        return questionGateway.getQuestionsList()
    }
}