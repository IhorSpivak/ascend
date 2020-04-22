package com.doneit.ascend.domain.gateway.gateway.data_source

import androidx.paging.PageKeyedDataSource
import com.doneit.ascend.domain.entity.SearchEntity
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.dto.ChatListDTO
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.source.storage.remote.repository.chats.IMyChatsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.ceil

class ChatSearchDataSource(
    private val scope: CoroutineScope,
    private val remote: IMyChatsRepository,
    private val requestDTO: ChatListDTO
) : PageKeyedDataSource<Int, ChatEntity>() {

    private var lastMMPage: Int? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ChatEntity>
    ) {
        scope.launch {
            try {

                var page = 1
                var chatCount: Int? = null

                val chats = remote.getMyChats(requestDTO.toRequest(page)).toResponseEntity(
                    {
                        chatCount = it?.count
                        it?.chats?.map { chat -> chat.toEntity() }
                    },
                    {
                        it?.errors
                    }
                )

                val res = mutableListOf<SearchEntity>()

                if (chatCount != null) {
                    val perPage = requestDTO.perPage ?: 10
                    lastMMPage = ceil(chatCount!!.toDouble() / perPage).toInt()
                } else {
                    lastMMPage = 0
                }

                if (chats.isSuccessful) {
                    callback.onResult(chats.successModel ?: listOf(), null, page + 1)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ChatEntity>) {
        scope.launch {
            try {

                val page = params.key

                val groups =
                    remote.getMyChats(requestDTO.toRequest(page)).toResponseEntity(
                        {
                            it?.chats?.map { attachment -> attachment.toEntity() }
                        },
                        {
                            it?.errors
                        }
                    )

                if (groups.isSuccessful) {
                    callback.onResult(groups.successModel ?: listOf(), page + 1)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ChatEntity>) {
    }
}