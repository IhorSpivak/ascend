package com.doneit.ascend.source.storage.remote.api

import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import com.doneit.ascend.source.storage.remote.data.response.group.WebinarQuestionsListResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface WebinarQuestionApi {

    @GET("groups/{id}/questions")
    fun getQuestions(
        @Path("id") id: Long,
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
        @Query("sort_column") sortColumn: String?,
        @Query("sort_type") sortType: String?,
        @Query("content") content: String?,
        @Query("user_id") userId: Long?,
        @Query("full_name") fullName: String?,
        @Query("created_at_from") createdAtFrom: String?,
        @Query("created_at_to") createdAtTo: String?,
        @Query("updated_at_from") updatedAtFrom: String?,
        @Query("updated_at_to") updatedAtTo: String?
    ): Deferred<Response<WebinarQuestionsListResponse>>

    @POST("groups/{id}/questions")
    fun createQuestion(@Path("id") id: Long): Deferred<Response<OKResponse>>

    @POST("groups/{id}/questions")
    fun createQuestion(
        @Path("id") id: Long,
        @Query("content") content: String
    ): Deferred<Response<OKResponse>>

    @PUT("webinar_questions/{id}")
    fun updateQuestion(
        @Path("id") id: Long,
        @Query("content") content: String
    ): Deferred<Response<OKResponse>>

    @DELETE("webinar_questions/{id}")
    fun deleteQuestion(
        @Path("id") id: Long
    ): Deferred<Response<OKResponse>>
}