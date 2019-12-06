package com.doneit.ascend.source.storage.remote.api

import com.doneit.ascend.source.storage.remote.data.request.CreateGroupRequest
import com.doneit.ascend.source.storage.remote.data.response.GroupResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface GroupApi {
    @POST("groups")
    fun createGroupAsync(@Body request: CreateGroupRequest): Deferred<Response<GroupResponse>>
}