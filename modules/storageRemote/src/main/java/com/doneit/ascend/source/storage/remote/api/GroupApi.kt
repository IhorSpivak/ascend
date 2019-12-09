package com.doneit.ascend.source.storage.remote.api

import com.doneit.ascend.source.storage.remote.data.response.GroupResponse
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface GroupApi {
    @Multipart
    @POST("groups")
    fun createGroupAsync(@Part part: List<MultipartBody.Part>): Deferred<Response<GroupResponse>>
}