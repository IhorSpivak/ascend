package com.doneit.ascend.source.storage.remote.api


import com.doneit.ascend.source.storage.remote.data.request.CreateChatRequest
import com.doneit.ascend.source.storage.remote.data.response.ChatResponse
import com.doneit.ascend.source.storage.remote.data.response.MyChatsListResponse
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface ChatApi {

    @GET("chats")
    fun getMyChatsListAsync(
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
        @Query("sort_column") sortColumn: String?,
        @Query("sort_type") sortType: String?,
        @Query("title") title: String?,
        @Query("created_at_from") createdAtFrom: String?,
        @Query("created_at_to") createdAtTo: String?,
        @Query("updated_at_from") updatedAtFrom: String?,
        @Query("updated_at_to") updatedAtTo: String?
    ): Deferred<Response<MyChatsListResponse>>

    @POST("chats")
    fun createChatAsync(@Body request: CreateChatRequest): Deferred<Response<ChatResponse>>

    @POST("chats/{id}")
    fun updateChatAsync(
        @Path("id") id: Long,
        @Body request: CreateChatRequest
    ): Deferred<Response<ChatResponse>>

    @POST("chats/{id}/message")
    fun sendMessageAsync(
        @Path("id") id: Long,
        @Query("message") message: String
    ): Deferred<Response<OKResponse>>


    //TODO:
    //@GET("chats/{id}/members")
    //fun getChatMembersAsync(@Path("id") id: Long)

    //TODO: add queries
    @GET("chats/{id}/messages")
    fun getChatMessagesAsync(
        @Path("id") id: Long
    ): Deferred<Response<MyChatsListResponse>>

    @DELETE("chats/{id}")
    fun deleteChatAsync(@Path("id") id: Long): Deferred<Response<OKResponse>>

    @DELETE("chats/{id}/leave")
    fun leaveChatAsync(@Path("id") id: Long): Deferred<Response<OKResponse>>

    @PUT("messages/{id}")
    fun editMessageAsync(
        @Path("id") id: Long,
        @Query("message") message: String
    ): Deferred<Response<OKResponse>>

    @POST("messages/{id}/read")
    fun markMessageAsReadAsync(
        @Path("id") id: Long,
        @Query("message") message: String
    ): Deferred<Response<OKResponse>>

    @DELETE("messages/{id}")
    fun deleteMessageAsync(
        @Path("id") id: Long,
        @Query("message") message: String
    ): Deferred<Response<OKResponse>>
}