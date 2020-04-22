package com.doneit.ascend.source.storage.remote.repository.chats

import com.doneit.ascend.source.storage.remote.data.request.*
import com.doneit.ascend.source.storage.remote.data.response.*
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse

interface IMyChatsRepository {
    suspend fun getMyChats(request: MyChatsListRequest): RemoteResponse<MyChatsListResponse, ErrorsListResponse>

    suspend fun getMessages(id: Long, request: MessageListRequest): RemoteResponse<MessagesListResponse, ErrorsListResponse>

    suspend fun getMembers(id: Long, request: MemberListRequest): RemoteResponse<MemberListResponse, ErrorsListResponse>

    suspend fun deleteChat(id: Long): RemoteResponse<OKResponse, ErrorsListResponse>

    suspend fun sendMessage(request: MessageRequest): RemoteResponse<OKResponse, ErrorsListResponse>

    suspend fun leaveChat(id: Long): RemoteResponse<OKResponse, ErrorsListResponse>

    suspend fun createChat(request: CreateChatRequest): RemoteResponse<ChatResponse, ErrorsListResponse>

    suspend fun updateChat(
        id: Long,
        title: String?,
        chatMembers: List<Int>?
    ): RemoteResponse<ChatResponse, ErrorsListResponse>
}