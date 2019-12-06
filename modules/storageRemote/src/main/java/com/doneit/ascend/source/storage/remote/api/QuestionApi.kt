package com.doneit.ascend.source.storage.remote.api

import com.doneit.ascend.source.storage.remote.data.response.QuestionsListResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
interface QuestionApi {
    @GET("questions")
    fun getQuestionsAsync() : Deferred<Response<QuestionsListResponse>>
}