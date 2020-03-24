package com.doneit.ascend.source.storage.remote.api

import com.doneit.ascend.source.storage.remote.data.request.SubscribeGroupRequest
import com.doneit.ascend.source.storage.remote.data.request.group.CancelGroupRequest
import com.doneit.ascend.source.storage.remote.data.request.group.InviteToGroupRequest
import com.doneit.ascend.source.storage.remote.data.request.group.UpdateNoteRequest
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import com.doneit.ascend.source.storage.remote.data.response.group.GroupCredentialsResponse
import com.doneit.ascend.source.storage.remote.data.response.group.GroupListResponse
import com.doneit.ascend.source.storage.remote.data.response.group.GroupResponse
import com.doneit.ascend.source.storage.remote.data.response.group.ParticipantListResponse
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface GroupApi {
    @Multipart
    @POST("groups")
    fun createGroupAsync(@Part part: List<MultipartBody.Part>): Deferred<Response<GroupResponse>>

    @Multipart
    @PUT("groups/{id}")
    fun updateGroupAsync(@Path("id") id: Long,@Part part: List<MultipartBody.Part>): Deferred<Response<GroupResponse>>

    @GET("groups")
    fun getGroupsAsync(
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
        @Query("sort_column") sortColumn: String?,
        @Query("sort_type") sortType: String?,
        @Query("name") name: String?,
        @Query("user_id") userId: Long?,
        @Query("group_type") groupType: String?,
        @Query("status") status: String?,
        @Query("my_groups") myGroups: Boolean?,
        @Query("start_time_from") startTimeFrom: String?,
        @Query("start_time_to") startTimeTo: String?
    ): Deferred<Response<GroupListResponse>>

    @GET("groups/{id}")
    fun getGroupDetailsAsync(@Path("id") id: Long): Deferred<Response<GroupResponse>>

    @DELETE("groups/{id}")
    fun deleteGroupAsync(@Path("id") id: Long): Deferred<Response<OKResponse>>

    @POST("groups/{groupId}/buy")
    fun subscribeAsync(@Path("groupId") groupId: Long, @Body request: SubscribeGroupRequest): Deferred<Response<OKResponse>>

    @POST("groups/{groupId}/credentials")
    fun getCredentialsAsync(@Path("groupId") groupId: Long): Deferred<Response<GroupCredentialsResponse>>

    @GET("groups/{groupId}/participants")
    fun getParticipantsAsync(
        @Path("groupId") groupId: Long,
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
        @Query("sort_column") sortColumn: String?,
        @Query("sort_type") sortType: String?,
        @Query("full_name") fullName: String?,
        @Query("connected") boolean: Boolean?
    ): Deferred<Response<ParticipantListResponse>>

    @POST("groups/{groupId}/note")
    fun updateNote(@Path("groupId") groupId: Long, @Body request: UpdateNoteRequest): Deferred<Response<OKResponse>>

    @POST("groups/{groupId}/cancel")
    fun cancelGroup(@Path("groupId") groupId: Long, @Body request: CancelGroupRequest): Deferred<Response<OKResponse>>

    @POST("groups/{groupId}/invite")
    fun inviteToGroup(@Path("groupId") groupId: Long, @Body request: InviteToGroupRequest): Deferred<Response<OKResponse>>

    @DELETE("groups/{groupId}/invite/{inviteId}")
    fun deleteInviteAsync(@Path("groupId") groupId: Long, @Path("inviteId") inviteId: Long): Deferred<Response<OKResponse>>
}