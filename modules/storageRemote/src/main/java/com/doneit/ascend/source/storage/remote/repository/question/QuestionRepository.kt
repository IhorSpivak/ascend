package com.doneit.ascend.source.storage.remote.repository.question

import com.doneit.ascend.source.storage.remote.api.QuestionApi
import com.doneit.ascend.source.storage.remote.data.response.QuestionsListResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse
import com.google.gson.Gson
import com.doneit.ascend.source.storage.remote.repository.base.BaseRepository

internal class QuestionRepository(
    gson: Gson,
    private val api: QuestionApi
) : BaseRepository(gson), IQuestionRepository {

    override suspend fun getList(sessionToken: String): RemoteResponse<QuestionsListResponse, ErrorsListResponse> {
        return execute({ api.getQuestionsAsync(sessionToken) }, ErrorsListResponse::class.java)
    }
}