package com.doneit.ascend.source.storage.remote.api


import com.doneit.ascend.source.storage.remote.data.request.CreateChatRequest
import com.doneit.ascend.source.storage.remote.data.response.*
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
        @Query("updated_at_to") updatedAtTo: String?,
        @Query("chat_type") chatType: String?
    ): Deferred<Response<MyChatsListResponse>>

    @GET("chats/{id}")
    fun getChatDetailAsync(
        @Path("id") id: Long
    ): Deferred<Response<ChatResponse>>

    @POST("chats")
    fun createChatAsync(@Body request: CreateChatRequest): Deferred<Response<ChatResponse>>

    @PUT("chats/{id}")
    fun updateChatAsync(
        @Path("id") id: Long,
        @Body request: CreateChatRequest
    ): Deferred<Response<ChatResponse>>

    @POST("chats/{id}/message")
    fun sendMessageAsync(
        @Path("id") id: Long,
        @Query("message") message: String
    ): Deferred<Response<OKResponse>>

    @GET("chats/{id}/members")
    fun getChatMembersAsync(
        @Path("id") id: Long,
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
        @Query("sort_column") sortColumn: String?,
        @Query("sort_type") sortType: String?,
        @Query("full_name") fullName: String?,
        @Query("online") online: Int?
    ): Deferred<Response<MemberListResponse>>

    @GET("chats/{id}/messages")
    fun getChatMessagesAsync(
        @Path("id") id: Long,
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
        @Query("sort_column") sortColumn: String?,
        @Query("sort_type") sortType: String?,
        @Query("message") message: String?,
        @Query("status") status: String?,
        @Query("user_id") userId: String?,
        @Query("edited") edited: Int?,
        @Query("created_at_from") createdAtFrom: String?,
        @Query("created_at_to") createdAtTo: String?,
        @Query("updated_at_from") updatedAtFrom: String?,
        @Query("updated_at_to") updatedAtTo: String?
    ): Deferred<Response<MessagesListResponse>>

    @DELETE("chats/{id}")
    fun deleteChatAsync(@Path("id") id: Long): Deferred<Response<OKResponse>>

    @DELETE("chats/{id}/leave")
    fun leaveChatAsync(@Path("id") id: Long): Deferred<Response<OKResponse>>

    @DELETE("chats/{id}/remove_user/{user_id}")
    fun removeUserAsync(
        @Path("id") chatId: Long,
        @Path("user_id") userId: Long
    ): Deferred<Response<OKResponse>>

    @PUT("messages/{id}")
    fun editMessageAsync(
        @Path("id") id: Long,
        @Query("message") message: String
    ): Deferred<Response<OKResponse>>

    @GET("messages/unread_messages_count")
    fun getUnreadMessageCount(): Deferred<Response<UnreadMessageCountResponse>>

    @POST("messages/{id}/read")
    fun markMessageAsReadAsync(
        @Path("id") id: Long
    ): Deferred<Response<OKResponse>>

    @DELETE("messages/{id}")
    fun deleteMessageAsync(
        @Path("id") id: Long
    ): Deferred<Response<OKResponse>>

    @GET("black_list")
    fun getBlockedUsersAsync(
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
        @Query("sort_column") sortColumn: String?,
        @Query("sort_type") sortType: String?,
        @Query("full_name") fullName: String?
    ): Deferred<Response<BlockedUsersResponse>>

    @POST("users/{id}/block")
    fun blockUserAsync(
        @Path("id") id: Long
    ): Deferred<Response<OKResponse>>

    @DELETE("users/{id}/unblock")
    fun unblockUserAsync(
        @Path("id") id: Long
    ): Deferred<Response<OKResponse>>

    @GET("/api/v1/chats/available_chats")
    fun getAvailableChatIds(): Deferred<Response<AvailableChatResponse>>
}