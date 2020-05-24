package com.doneit.ascend.source.storage.remote.repository.group.webinar_questions

import com.doneit.ascend.source.storage.remote.api.WebinarQuestionApi
import com.doneit.ascend.source.storage.remote.data.request.WebinarQuestionsListRequest
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse
import com.doneit.ascend.source.storage.remote.data.response.group.WebinarQuestionsListResponse
import com.doneit.ascend.source.storage.remote.repository.base.BaseRepository
import com.google.gson.Gson

internal class WebinarQuestionsRepository(
    gson: Gson,
    private val api: WebinarQuestionApi
) : BaseRepository(gson), IWebinarQuestionsRepository {
    override suspend fun getQuestions(
        groupId: Long,
        request: WebinarQuestionsListRequest
    ): RemoteResponse<WebinarQuestionsListResponse, ErrorsListResponse> {
        return execute({
            api.getQuestions(
                groupId,
                request.page,
                request.perPage,
                request.sortColumn,
                request.sortType,
                request.content,
                request.userId,
                request.fullName,
                request.createdAtFrom,
                request.createdAtTo,
                request.updatedAtFrom,
                request.updatedAtTo
            )
        }, ErrorsListResponse::class.java)
    }

    override suspend fun createQuestion(
        groupId: Long,
        content: String
    ): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.createQuestion(groupId, content) }, ErrorsListResponse::class.java)
    }

    override suspend fun updateQuestion(
        id: Long,
        content: String
    ): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.updateQuestion(id, content) }, ErrorsListResponse::class.java)
    }

    override suspend fun deleteQuestion(
        id: Long,
        content: String
    ): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.deleteQuestion(id) }, ErrorsListResponse::class.java)
    }
}