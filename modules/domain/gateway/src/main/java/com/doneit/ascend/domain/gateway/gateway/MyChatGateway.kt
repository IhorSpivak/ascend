package com.doneit.ascend.domain.gateway.gateway

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.ChatListDTO
import com.doneit.ascend.domain.entity.dto.CreateChatDTO
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_locale.toLocal
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.gateway.gateway.boundaries.MyChatsBoundaryCallback
import com.doneit.ascend.domain.use_case.gateway.IMyChatGateway
import com.doneit.ascend.source.storage.remote.repository.chats.IMyChatsRepository
import com.vrgsoft.networkmanager.NetworkManager
import kotlinx.coroutines.GlobalScope
import java.util.concurrent.Executors

class MyChatGateway(
    private val local: com.doneit.ascend.source.storage.local.repository.chats.IMyChatsRepository,
    private val remote: IMyChatsRepository,
    errors: NetworkManager
) : BaseGateway(errors), IMyChatGateway {
    override fun getMyChatList(request: ChatListDTO): LiveData<PagedList<ChatEntity>> =
        liveData<PagedList<ChatEntity>> {
            val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(request.perPage ?: 10)
                .build()
            val factory = local.getList().map { it.toEntity() }

            val boundary = MyChatsBoundaryCallback(
                GlobalScope,
                local,
                remote,
                request
            )

            emitSource(
                LivePagedListBuilder<Int, ChatEntity>(factory, config)
                    .setFetchExecutor(Executors.newSingleThreadExecutor())
                    .setBoundaryCallback(boundary)
                    .build()
            )

            boundary.loadInitial()
        }

    override suspend fun delete(id: Long): ResponseEntity<Unit, List<String>> {
        val result = executeRemote { remote.deleteChat(id) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )

        if (result.isSuccessful) {
            local.remove(id)
        }

        return result
    }

    override suspend fun leave(id: Long): ResponseEntity<Unit, List<String>> {
        val result = executeRemote { remote.leaveChat(id) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )

        if (result.isSuccessful) {
            local.remove(id)
        }

        return result
    }

    override suspend fun createChat(createChatDTO: CreateChatDTO): ResponseEntity<ChatEntity, List<String>> {
        val result = executeRemote {
            remote.createChat(
                createChatDTO.toRequest()
            )
        }.toResponseEntity(
            {
                it?.toEntity()
            },
            {
                it?.errors
            }
        )

        if (result.isSuccessful) {
            local.insert(result.successModel!!.toLocal())
        }

        return result
    }

    override suspend fun updateChat(
        id: Long,
        createChatDTO: CreateChatDTO
    ): ResponseEntity<ChatEntity, List<String>> {
        val result = executeRemote {
            remote.updateChat(
                id,
                createChatDTO.toRequest()
            )
        }.toResponseEntity(
            {
                it?.toEntity()
            },
            {
                it?.errors
            }
        )

        if (result.isSuccessful) {
            local.insert(result.successModel!!.toLocal())
        }

        return result
    }
}