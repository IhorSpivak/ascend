package com.doneit.ascend.source.storage.remote.repository.question

import com.doneit.ascend.source.storage.remote.api.QuestionApi
import com.doneit.ascend.source.storage.remote.data.response.QuestionsListResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse
import com.doneit.ascend.source.storage.remote.repository.base.BaseRepository
import com.google.gson.Gson

internal class QuestionRepository(
    gson: Gson,
    private val api: QuestionApi
) : BaseRepository(gson), IQuestionRepository {

    override suspend fun getList(): RemoteResponse<QuestionsListResponse, ErrorsListResponse> {
        return execute({ api.getQuestionsAsync() }, ErrorsListResponse::class.java)
    }
}