package com.doneit.ascend.domain.gateway.gateway

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.WebinarQuestionEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.WebinarQuestionDTO
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.use_case.gateway.IWebinarQuestionGateway
import com.doneit.ascend.domain.use_case.interactor.webinar_questions.WebinarQuestionUseCase
import com.doneit.ascend.source.storage.remote.repository.group.webinar_questions.IWebinarQuestionsRepository
import com.vrgsoft.networkmanager.NetworkManager

class WebinarQuestionGateway(
    private val remote: IWebinarQuestionsRepository,
    private val useCase: WebinarQuestionUseCase,
    errors: NetworkManager
) : BaseGateway(errors), IWebinarQuestionGateway {
    override fun getQuestions(groupId: Long, request: WebinarQuestionDTO): LiveData<PagedList<WebinarQuestionEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun create(
        groupId: Long,
        content: String
    ): ResponseEntity<Unit, List<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun update(id: Long, content: String): ResponseEntity<Unit, List<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: Long): ResponseEntity<Unit, List<String>> {
        TODO("Not yet implemented")
    }

}