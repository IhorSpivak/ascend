package com.doneit.ascend.source.storage.remote.api

import com.doneit.ascend.source.storage.remote.data.response.MasterMindListResponse
import com.doneit.ascend.source.storage.remote.data.response.MasterMindResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MasterMindApi {

    @GET("users")
    fun getMasterMindsList(@Query("page") page: Int?,
                           @Query("per_page") perPage: Int?,
                           @Query("sort_column") sortColumn: String?,
                           @Query("sort_type") sortType: String?,
                           @Query("full_name") fullName: String?,
                           @Query("display_name") displayName: String?,
                           @Query("followed") followed: Boolean?,
                           @Query("rated") rated: Boolean?): Deferred<Response<MasterMindListResponse>>

    @GET("users/{id}")
    fun getProfile(@Path("id")id: Long): Deferred<Response<MasterMindResponse>>
}