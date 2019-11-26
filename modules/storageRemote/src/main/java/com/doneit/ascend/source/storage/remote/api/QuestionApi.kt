package com.doneit.ascend.source.storage.remote.api

import com.doneit.ascend.source.storage.remote.data.response.QuestionsListResponse
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
interface QuestionApi {
    @GET("questions")
    fun getQuestionsAsync(@Header("Session-Token") sessionToken: String) : Deferred<Response<QuestionsListResponse>>
}