package com.doneit.ascend.source.storage.remote.repository.answer

import com.doneit.ascend.source.storage.remote.data.request.AnswerRequest
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse

interface IAnswerRepository {
    suspend fun createAnswers(answers: AnswerRequest): RemoteResponse<OKResponse, ErrorsListResponse>
}