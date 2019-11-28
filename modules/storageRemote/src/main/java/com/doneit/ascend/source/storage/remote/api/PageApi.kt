package com.doneit.ascend.source.storage.remote.api

import com.doneit.ascend.source.storage.remote.data.response.PageResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PageApi {
    @GET("pages/{page_type}")
    fun getPageAsync(@Path("page_type") pageType: String) : Deferred<Response<PageResponse>>
}