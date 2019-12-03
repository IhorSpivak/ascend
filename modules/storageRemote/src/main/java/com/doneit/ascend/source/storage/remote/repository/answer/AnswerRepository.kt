package com.doneit.ascend.source.storage.remote.repository.answer

import com.doneit.ascend.source.storage.remote.api.AnswerApi
import com.doneit.ascend.source.storage.remote.data.request.AnswerRequest
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse
import com.doneit.ascend.source.storage.remote.repository.base.BaseRepository
import com.google.gson.Gson

internal class AnswerRepository(
    gson: Gson,
    private val api: AnswerApi
) : BaseRepository(gson), IAnswerRepository {

    override suspend fun createAnswers(
        sessionToken: String,
        answers: AnswerRequest
    ): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute(
            { api.createAnswersAsync(sessionToken, answers) },
            ErrorsListResponse::class.java
        )
    }
}