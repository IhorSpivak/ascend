package com.doneit.ascend.source.storage.remote.repository.answer

import com.doneit.ascend.source.storage.remote.data.request.AnswerRequest
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse
import retrofit2.http.Header

interface IAnswerRepository {
    suspend fun createAnswers(@Header("Session-Token") sessionToken: String, answers: List<AnswerRequest>): RemoteResponse<OKResponse, ErrorsListResponse>
}