package com.doneit.ascend.source.storage.remote.api

import com.doneit.ascend.source.storage.remote.data.request.RatingRequest
import com.doneit.ascend.source.storage.remote.data.request.ReportRequest
import com.doneit.ascend.source.storage.remote.data.response.MasterMindListResponse
import com.doneit.ascend.source.storage.remote.data.response.MasterMindResponse
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface MasterMindApi {

    @GET("users")
    fun getMasterMindsListAsync(
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
        @Query("sort_column") sortColumn: String?,
        @Query("sort_type") sortType: String?,
        @Query("full_name") fullName: String?,
        @Query("display_name") displayName: String?,
        @Query("followed") followed: Boolean?,
        @Query("rated") rated: Boolean?
    ): Deferred<Response<MasterMindListResponse>>

    @GET("users/{user_id}")
    fun getProfileAsync(@Path("user_id") id: Long): Deferred<Response<MasterMindResponse>>

    @POST("users/{id}/follow")
    fun followAsync(@Path("id") id: Long): Deferred<Response<OKResponse>>

    @DELETE("users/{user_id}/unfollow")
    fun unfollowAsync(@Path("user_id") id: Long): Deferred<Response<OKResponse>>

    @POST("users/{user_id}/rating")
    fun setRatingAsync(@Path("user_id") id: Long, @Body request: RatingRequest): Deferred<Response<OKResponse>>

    @POST("users/{user_id}/report")
    fun sendReportAsync(@Path("user_id") id: Long, @Body request: ReportRequest): Deferred<Response<OKResponse>>
}