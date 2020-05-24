package com.doneit.ascend.domain.use_case.interactor.webinar_questions

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.WebinarQuestionEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.WebinarQuestionDTO
import com.doneit.ascend.domain.use_case.gateway.IWebinarQuestionGateway

class WebinarQuestionInteractor(
    private val gateway: IWebinarQuestionGateway
) : WebinarQuestionUseCase {
    override fun getWebinarQuestionLive(groupId: Long, request: WebinarQuestionDTO): LiveData<PagedList<WebinarQuestionEntity>> {
        return gateway.getQuestions(groupId, request)
    }

    override suspend fun createQuestion(
        groupId: Long,
        content: String
    ): ResponseEntity<Unit, List<String>> {
        return gateway.create(groupId, content)
    }

    override suspend fun updateQuestion(
        id: Long,
        content: String
    ): ResponseEntity<Unit, List<String>> {
        return gateway.update(id, content)
    }

    override suspend fun deleteQuestion(id: Long): ResponseEntity<Unit, List<String>> {
        return gateway.delete(id)
    }

}