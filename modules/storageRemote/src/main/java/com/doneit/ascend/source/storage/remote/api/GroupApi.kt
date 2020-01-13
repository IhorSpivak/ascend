package com.doneit.ascend.source.storage.remote.api

import com.doneit.ascend.source.storage.remote.data.request.SubscribeGroupRequest
import com.doneit.ascend.source.storage.remote.data.response.GroupCredentialsResponse
import com.doneit.ascend.source.storage.remote.data.response.GroupListResponse
import com.doneit.ascend.source.storage.remote.data.response.GroupResponse
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface GroupApi {
    @Multipart
    @POST("groups")
    fun createGroupAsync(@Part part: List<MultipartBody.Part>): Deferred<Response<GroupResponse>>

    @GET("groups")
    fun getGroupsAsync(
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
        @Query("sort_column") sortColumn: String?,
        @Query("sort_type") sortType: String?,
        @Query("name") name: String?,
        @Query("user_id") userId: Long?,
        @Query("group_type") groupType: String?,
        @Query("my_groups") myGroups: Boolean?
    ): Deferred<Response<GroupListResponse>>

    @GET("groups/{id}")
    fun getGroupDetailsAsync(@Path("id") id: Long): Deferred<Response<GroupResponse>>

    @DELETE("groups/{id}")
    fun deleteGroupAsync(@Path("id") id: Long) : Deferred<Response<OKResponse>>

    @POST("groups/{groupId}/buy")
    fun subscribeAsync(@Path("groupId") groupId: Long, @Body request: SubscribeGroupRequest): Deferred<Response<OKResponse>>

    @POST("groups/{groupId}/credentials")
    fun getCredentials(@Path("groupId") groupId: Long): Deferred<Response<GroupCredentialsResponse>>
}