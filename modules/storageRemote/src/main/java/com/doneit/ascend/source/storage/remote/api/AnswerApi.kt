package com.doneit.ascend.source.storage.remote.api

import com.doneit.ascend.source.storage.remote.data.request.AnswerRequest
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AnswerApi {
    @POST("answers")
    fun createAnswersAsync(@Body answers: AnswerRequest) : Deferred<Response<OKResponse>>
}