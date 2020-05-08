package com.doneit.ascend.source.storage.remote.repository.chats

import com.doneit.ascend.source.storage.remote.api.ChatApi
import com.doneit.ascend.source.storage.remote.data.request.*
import com.doneit.ascend.source.storage.remote.data.response.*
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse
import com.doneit.ascend.source.storage.remote.repository.base.BaseRepository
import com.google.gson.Gson

internal class MyChatsRepository(
    gson: Gson,
    private val api: ChatApi
) : BaseRepository(gson), IMyChatsRepository {
    override suspend fun getMyChats(request: MyChatsListRequest): RemoteResponse<MyChatsListResponse, ErrorsListResponse> {
        return execute({
            api.getMyChatsListAsync(
                request.page,
                request.perPage,
                request.sortColumn,
                request.sortType,
                request.title,
                request.createdAtFrom,
                request.createdAtTo,
                request.updatedAtFrom,
                request.updatedAtTo
            )
        }, ErrorsListResponse::class.java)
    }

    override suspend fun getMessages(
        id: Long,
        request: MessageListRequest
    ): RemoteResponse<MessagesListResponse, ErrorsListResponse> {
        return execute({
            api.getChatMessagesAsync(
                id,
                request.page,
                request.perPage,
                request.sortColumn,
                request.sortType,
                request.message,
                request.status,
                request.userId,
                request.edited,
                request.createdAtFrom,
                request.createdAtTo,
                request.updatedAtFrom,
                request.updatedAtTo
            )
        }, ErrorsListResponse::class.java)
    }

    override suspend fun getMembers(
        id: Long,
        request: MemberListRequest
    ): RemoteResponse<MemberListResponse, ErrorsListResponse> {
        return execute({
            api.getChatMembersAsync(
                id,
                request.page,
                request.perPage,
                request.sortColumn,
                request.sortType,
                request.fullName,
                request.online
            )
        }, ErrorsListResponse::class.java)
    }

    override suspend fun deleteChat(id: Long): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.deleteChatAsync(id) }, ErrorsListResponse::class.java)
    }

    override suspend fun sendMessage(request: MessageRequest): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute(
            { api.sendMessageAsync(request.id, request.message) },
            ErrorsListResponse::class.java
        )
    }

    override suspend fun leaveChat(id: Long): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.leaveChatAsync(id) }, ErrorsListResponse::class.java)
    }

    override suspend fun createChat(request: CreateChatRequest): RemoteResponse<ChatResponse, ErrorsListResponse> {
        return execute({ api.createChatAsync(request) }, ErrorsListResponse::class.java)
    }

    override suspend fun updateChat(
        id: Long,
        title: String?,
        chatMembers: List<Long>?
    ): RemoteResponse<ChatResponse, ErrorsListResponse> {
        return execute(
            { api.updateChatAsync(id, CreateChatRequest(title, chatMembers?.map { it.toInt() })) },
            ErrorsListResponse::class.java
        )
    }

    override suspend fun blockUser(userId: Long): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.blockUserAsync(userId) }, ErrorsListResponse::class.java)
    }

    override suspend fun unblockUser(userId: Long): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.unblockUserAsync(userId) }, ErrorsListResponse::class.java)
    }

    override suspend fun getBlockedUsers(request: BlockedUsersRequest): RemoteResponse<BlockedUsersResponse, ErrorsListResponse> {
        return execute({
            api.getBlockedUsersAsync(
                request.page,
                request.perPage,
                request.sortColumn,
                request.sortType,
                request.fullName
            )
        }, ErrorsListResponse::class.java)
    }

    override suspend fun deleteMessage(messageId: Long): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.deleteMessageAsync(messageId) }, ErrorsListResponse::class.java)
    }

    override suspend fun markMessageAsRead(messageId: Long): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.markMessageAsReadAsync(messageId) }, ErrorsListResponse::class.java)
    }

    override suspend fun getAvailableChats(): RemoteResponse<AvailableChatResponse, ErrorsListResponse> {
        return execute({ api.getAvailableChatIds() }, ErrorsListResponse::class.java)
    }

    override suspend fun getUnreadMessageCount(): RemoteResponse<UnreadMessageCountResponse, ErrorsListResponse> {
        return execute({ api.getUnreadMessageCount()}, ErrorsListResponse::class.java)
    }
}