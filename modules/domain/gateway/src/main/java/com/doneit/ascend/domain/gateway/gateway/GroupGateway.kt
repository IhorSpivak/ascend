package com.doneit.ascend.domain.gateway.gateway

import android.accounts.AccountManager
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.CreateGroupModel
import com.doneit.ascend.domain.entity.dto.GroupCredentialsModel
import com.doneit.ascend.domain.entity.dto.GroupListModel
import com.doneit.ascend.domain.entity.dto.SubscribeGroupModel
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toCreateGroupRequest
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.gateway.gateway.data_source.GroupDataSource
import com.doneit.ascend.domain.use_case.gateway.IGroupGateway
import com.doneit.ascend.source.storage.remote.data.request.GroupSocketCookies
import com.doneit.ascend.source.storage.remote.repository.group.IGroupRepository
import com.doneit.ascend.source.storage.remote.repository.group.socket.IGroupSocketRepository
import com.vrgsoft.networkmanager.NetworkManager
import com.vrgsoft.networkmanager.livedata.SingleLiveEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.io.File

internal class GroupGateway(
    errors: NetworkManager,
    private val remote: IGroupRepository,
    private val remoteSocket: IGroupSocketRepository,
    private val accountManager: AccountManager,//todo move logic with account manager to corresponding Repository
    private val packageName: String
) : BaseGateway(errors), IGroupGateway {

    override fun <T> calculateMessage(error: T): String {
        return ""//todo, not required for now
    }

    override val messagesStream = SingleLiveEvent<String?>()

    override suspend fun createGroup(groupModel: CreateGroupModel): ResponseEntity<GroupEntity, List<String>> {
        return executeRemote {
            remote.createGroup(
                File(groupModel.imagePath),
                groupModel.toCreateGroupRequest()
            )
        }.toResponseEntity(
            {
                it?.toEntity()
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun getGroupsList(groupListModel: GroupListModel): ResponseEntity<List<GroupEntity>, List<String>> {
        return remote.getGroupsList(groupListModel.toRequest()).toResponseEntity(
            {
                it?.groups?.map { it.toEntity() }
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun getGroupsListPaged(groupListModel: GroupListModel): PagedList<GroupEntity> {

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(groupListModel.perPage ?: 10)
            .build()

        val dataSource = GroupDataSource(
            GlobalScope,
            remote,
            groupListModel
        )
        val executor = MainThreadExecutor()

        return PagedList.Builder<Int, GroupEntity>(dataSource, config)
            .setFetchExecutor(executor)
            .setNotifyExecutor(executor)
            .build()
    }

    override suspend fun getGroupDetails(groupId: Long): ResponseEntity<GroupEntity, List<String>> {
        return executeRemote { remote.getGroupDetails(groupId) }.toResponseEntity(
            {
                it?.toEntity()
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun deleteGroup(groupId: Long): ResponseEntity<Unit, List<String>> {
        return executeRemote { remote.deleteGroup(groupId) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun subscribe(model: SubscribeGroupModel): ResponseEntity<Unit, List<String>> {
        return remote.subscribe(model.groupId, model.toRequest()).toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun getCredentials(groupId: Long): ResponseEntity<GroupCredentialsModel, List<String>> {
        return remote.getCredentials(groupId).toResponseEntity(
            {
                it?.toEntity()
            },
            {
                it?.errors
            }
        )
    }

    override fun connectToChannel(groupId: Long) {
        //todo should use another scope?
        GlobalScope.launch {
            val accounts = accountManager.getAccountsByType(packageName)

            if (accounts.isNotEmpty()) {
                val account = accounts[0]
                val token = accountManager.blockingGetAuthToken(account, "Bearer", false)
                val cookies = GroupSocketCookies(token, groupId)
                remoteSocket.connect(getWebSocketAdapter(), cookies)
            }
        }
    }

    override fun disconnect() {
        remoteSocket.disconnect()
    }


    private fun getWebSocketAdapter(): WebSocketListener {
        return object: WebSocketListener() {
            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                messagesStream.postValue(text)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                messagesStream.postValue(bytes.toString())
            }

            override fun onOpen(webSocket: WebSocket, response: Response) {
                remoteSocket.sendMessage(SUBSCRIBE_GROUP_CHANNEL_COMMAND)
            }
        }
    }

    companion object {
        private const val SUBSCRIBE_GROUP_CHANNEL_COMMAND = "{\"command\": \"subscribe\",\"identifier\":\"{\"channel\":\"GroupChannel\"}\"}"
    }
}