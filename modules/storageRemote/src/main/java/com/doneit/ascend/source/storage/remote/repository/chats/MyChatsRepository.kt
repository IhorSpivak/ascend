package com.doneit.ascend.source.storage.remote.repository.chats

import com.doneit.ascend.source.storage.remote.api.ChatApi
import com.doneit.ascend.source.storage.remote.data.request.CreateChatRequest
import com.doneit.ascend.source.storage.remote.data.request.MyChatsListRequest
import com.doneit.ascend.source.storage.remote.data.response.ChatResponse
import com.doneit.ascend.source.storage.remote.data.response.MyChatsListResponse
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
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

    override suspend fun deleteChat(id: Long): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.deleteChatAsync(id) }, ErrorsListResponse::class.java)
    }

    override suspend fun leaveChat(id: Long): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.leaveChatAsync(id) }, ErrorsListResponse::class.java)
    }

    override suspend fun createChat(request: CreateChatRequest): RemoteResponse<ChatResponse, ErrorsListResponse> {
        return execute({ api.createChatAsync(request) }, ErrorsListResponse::class.java)
    }

    override suspend fun updateChat(
        id: Long,
        request: CreateChatRequest
    ): RemoteResponse<ChatResponse, ErrorsListResponse> {
        return execute({ api.updateChatAsync(id, request) }, ErrorsListResponse::class.java)
    }

}