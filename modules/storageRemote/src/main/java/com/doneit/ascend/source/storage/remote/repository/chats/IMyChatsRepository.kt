package com.doneit.ascend.source.storage.remote.repository.chats

import com.doneit.ascend.source.storage.remote.data.request.CreateChatRequest
import com.doneit.ascend.source.storage.remote.data.request.MyChatsListRequest
import com.doneit.ascend.source.storage.remote.data.response.ChatResponse
import com.doneit.ascend.source.storage.remote.data.response.MyChatsListResponse
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse

interface IMyChatsRepository {
    suspend fun getMyChats(request: MyChatsListRequest): RemoteResponse<MyChatsListResponse, ErrorsListResponse>

    suspend fun deleteChat(id: Long): RemoteResponse<OKResponse, ErrorsListResponse>

    suspend fun leaveChat(id: Long): RemoteResponse<OKResponse, ErrorsListResponse>

    suspend fun createChat(request: CreateChatRequest): RemoteResponse<ChatResponse, ErrorsListResponse>

    suspend fun updateChat(
        id: Long,
        request: CreateChatRequest
    ): RemoteResponse<ChatResponse, ErrorsListResponse>
}