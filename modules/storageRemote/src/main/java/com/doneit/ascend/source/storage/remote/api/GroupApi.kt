package com.doneit.ascend.source.storage.remote.api

import com.doneit.ascend.source.storage.remote.data.response.GroupListResponse
import com.doneit.ascend.source.storage.remote.data.response.GroupResponse
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface GroupApi {
    @Multipart
    @POST("groups")
    fun createGroupAsync(@Part part: List<MultipartBody.Part>): Deferred<Response<GroupResponse>>

    @GET("groups")
    fun getGroupsAsync(@Header("page") page: Int?,
                       @Header("per_page") perPage: Int?,
                       @Header("sort_column") sortColumn: String?,
                       @Header("sort_type") sortType: String?,
                       @Header("name") name: String?,
                       @Header("user_id") userId: Long?,
                       @Header("group_type") groupType: String?,
                       @Header("my_groups") myGroups: Boolean?): Deferred<Response<GroupListResponse>>
}