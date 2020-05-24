package com.doneit.ascend.source.storage.remote.repository.group.webinar_questions

import com.doneit.ascend.source.storage.remote.data.request.WebinarQuestionsListRequest
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse
import com.doneit.ascend.source.storage.remote.data.response.group.WebinarQuestionsListResponse

interface IWebinarQuestionsRepository {

    suspend fun getQuestions(
        groupId: Long,
        webinarQuestionsListRequest: WebinarQuestionsListRequest
    ): RemoteResponse<WebinarQuestionsListResponse, ErrorsListResponse>

    suspend fun createQuestion(
        groupId: Long,
        content: String
    ): RemoteResponse<OKResponse, ErrorsListResponse>

    suspend fun updateQuestion(
        id: Long,
        content: String
    ): RemoteResponse<OKResponse, ErrorsListResponse>

    suspend fun deleteQuestion(
        id: Long,
        content: String
    ): RemoteResponse<OKResponse, ErrorsListResponse>
}