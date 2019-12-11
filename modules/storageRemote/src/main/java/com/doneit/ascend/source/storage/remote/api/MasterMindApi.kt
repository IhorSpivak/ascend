package com.doneit.ascend.source.storage.remote.api

import com.doneit.ascend.source.storage.remote.data.response.MasterMindListResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface MasterMindApi {

    @GET("users")
    fun getMasterMindsList(@Header("page") page: Int?,
                       @Header("per_page") perPage: Int?,
                       @Header("sort_column") sortColumn: String?,
                       @Header("sort_type") sortType: String?,
                       @Header("full_name") fullName: String?,
                       @Header("display_name") displayName: String?,
                       @Header("followed") followed: Boolean?,
                       @Header("rated") rated: Boolean?): Deferred<Response<MasterMindListResponse>>
}