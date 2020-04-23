package com.doneit.ascend.domain.gateway.gateway

import android.accounts.AccountManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.chats.BlockedUserEntity
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.*
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_locale.toLocal
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.gateway.gateway.boundaries.BlockedUsersBoundaryCallback
import com.doneit.ascend.domain.gateway.gateway.boundaries.MembersBoundaryCallback
import com.doneit.ascend.domain.gateway.gateway.boundaries.MessagesBoundaryCallback
import com.doneit.ascend.domain.gateway.gateway.boundaries.MyChatsBoundaryCallback
import com.doneit.ascend.domain.use_case.gateway.IMyChatGateway
import com.doneit.ascend.source.storage.remote.data.request.group.ChatSocketCookies
import com.doneit.ascend.source.storage.remote.repository.chats.IMyChatsRepository
import com.doneit.ascend.source.storage.remote.repository.chats.socket.IChatSocketRepository
import com.vrgsoft.networkmanager.NetworkManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class MyChatGateway(
    private val local: com.doneit.ascend.source.storage.local.repository.chats.IMyChatsRepository,
    private val remote: IMyChatsRepository,
    private val remoteSocket: IChatSocketRepository,
    private val accountManager: AccountManager,
    private val packageName: String,
    errors: NetworkManager
) : BaseGateway(errors), IMyChatGateway {
    override fun getMyChatListLive(request: ChatListDTO): LiveData<PagedList<ChatEntity>> =
        liveData<PagedList<ChatEntity>> {
            val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(request.perPage ?: 10)
                .build()
            val factory = local.getList(request.title).map { it.toEntity() }

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

    override fun getMessages(
        chatId: Long,
        request: MessageListDTO
    ): LiveData<PagedList<MessageEntity>> =
        liveData<PagedList<MessageEntity>> {
            val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(request.perPage ?: 10)
                .build()
            val factory = local.getMessageList(chatId).map { it.toEntity() }

            val boundary = MessagesBoundaryCallback(
                GlobalScope,
                local,
                remote,
                request,
                chatId
            )

            emitSource(
                LivePagedListBuilder<Int, MessageEntity>(factory, config)
                    .setFetchExecutor(Executors.newSingleThreadExecutor())
                    .setBoundaryCallback(boundary)
                    .build()
            )

            boundary.loadInitial()
        }

    override fun getMembers(
        chatId: Long,
        request: MemberListDTO
    ): LiveData<PagedList<MemberEntity>> =
        liveData<PagedList<MemberEntity>> {
            val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(request.perPage ?: 50)
                .build()
            val factory = local.getMemberList().map { it.toEntity() }

            val boundary = MembersBoundaryCallback(
                GlobalScope,
                local,
                remote,
                request,
                chatId
            )

            emitSource(
                LivePagedListBuilder<Int, MemberEntity>(factory, config)
                    .setFetchExecutor(Executors.newSingleThreadExecutor())
                    .setBoundaryCallback(boundary)
                    .build()
            )

            boundary.loadInitial()
        }

    override suspend fun getMemberList(
        chatId: Long,
        request: MemberListDTO
    ): ResponseEntity<List<MemberEntity>, List<String>> {
        val result = executeRemote {
            remote.getMembers(chatId, request.toRequest(1))
        }.toResponseEntity(
            {
                it?.users?.map { it.toEntity() }
            },
            {
                it?.errors
            }
        )

        if (result.isSuccessful) {
            local.insertAllMembers(result.successModel!!.map { it.toLocal() })
        }

        return result
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

    override suspend fun sendMessage(request: MessageDTO): ResponseEntity<Unit, List<String>> {
        return executeRemote { remote.sendMessage(request.toRequest()) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
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
        title: String?,
        chatMembers: List<Long>?
    ): ResponseEntity<ChatEntity, List<String>> {
        val result = executeRemote {
            remote.updateChat(
                id,
                title,
                chatMembers
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

    override val messagesStream = remoteSocket.messagesStream.map { it?.toEntity() }

    override fun connectToChannel(id: Long) {
        GlobalScope.launch {

            val accounts = accountManager.getAccountsByType(packageName)

            if (accounts.isNotEmpty()) {
                val account = accounts[0]
                val token = accountManager.blockingGetAuthToken(account, "Bearer", false)
                val cookies =
                    ChatSocketCookies(
                        token,
                        id
                    )
                remoteSocket.connect(cookies)
            }
        }
    }

    override fun disconnect() {
        remoteSocket.disconnect()
    }

    override fun insertMessage(message: MessageEntity, chatId: Long) {
        GlobalScope.launch {
            local.insertMessage(message.toLocal(chatId))
        }
    }

    override fun removeMessageLocal(message: MessageEntity) {
        GlobalScope.launch {
            local.removeMessage(message.id)
        }
    }

    override fun removeBlockedUser(userEntity: BlockedUserEntity) {
        GlobalScope.launch {
            local.removeBlockedUser(userEntity.id)
        }
    }

    override suspend fun blockUser(userId: Long): ResponseEntity<Unit, List<String>> {
        return executeRemote { remote.blockUser(userId) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun removeMessageRemote(messageId: Long): ResponseEntity<Unit, List<String>> {
        return executeRemote { remote.deleteMessage(messageId) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun unblockUser(userId: Long): ResponseEntity<Unit, List<String>> {
        return executeRemote { remote.unblockUser(userId) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }

    override fun getBlockedUsersLive(blockedUsersDTO: BlockedUsersDTO): LiveData<PagedList<BlockedUserEntity>> =
        liveData<PagedList<BlockedUserEntity>> {
            val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(blockedUsersDTO.perPage ?: 50)
                .build()
            val factory = local.getBlockedUsersLive().map { it.toEntity() }

            val boundary = BlockedUsersBoundaryCallback(
                GlobalScope,
                local,
                remote,
                blockedUsersDTO
            )

            emitSource(
                LivePagedListBuilder<Int, BlockedUserEntity>(factory, config)
                    .setFetchExecutor(Executors.newSingleThreadExecutor())
                    .setBoundaryCallback(boundary)
                    .build()
            )

            boundary.loadInitial()
        }
}