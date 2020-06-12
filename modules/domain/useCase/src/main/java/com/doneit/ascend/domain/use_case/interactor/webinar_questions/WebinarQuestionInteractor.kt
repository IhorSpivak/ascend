package com.doneit.ascend.domain.use_case.interactor.webinar_questions

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.WebinarQuestionDTO
import com.doneit.ascend.domain.entity.webinar_question.WebinarQuestionEntity
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

    override val questionStream = gateway.questionStream
    override fun connectToChannel(groupId: Long) {
        gateway.connectToChannel(groupId)
    }

    override fun disconnect() {
        gateway.disconnect()
    }

    override fun insertMessage(questionEntity: WebinarQuestionEntity) {
        return gateway.insertQuestion(questionEntity)
    }

    override fun removeQuestionsLocalExcept(groupId: Long) {
        return gateway.removeQuestionsLocalExcept(groupId)
    }


}