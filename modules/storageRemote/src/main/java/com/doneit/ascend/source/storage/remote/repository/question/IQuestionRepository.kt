package com.doneit.ascend.source.storage.remote.repository.question

import com.doneit.ascend.source.storage.remote.data.response.QuestionsListResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse
import retrofit2.http.Header

interface IQuestionRepository {
    suspend fun getList(@Header("Session-Token") sessionToken: String): RemoteResponse<QuestionsListResponse, ErrorsListResponse>
}