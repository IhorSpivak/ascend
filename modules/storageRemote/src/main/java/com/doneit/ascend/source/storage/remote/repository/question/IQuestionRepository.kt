package com.doneit.ascend.source.storage.remote.repository.question

import com.doneit.ascend.source.storage.remote.data.response.QuestionsListResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse

interface IQuestionRepository {
    suspend fun getList(): RemoteResponse<QuestionsListResponse, ErrorsListResponse>
}