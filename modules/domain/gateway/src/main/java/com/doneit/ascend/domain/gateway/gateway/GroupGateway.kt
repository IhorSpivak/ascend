package com.doneit.ascend.domain.gateway.gateway

import android.accounts.AccountManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.ParticipantEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.*
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_locale.toLocal
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toCreateGroupRequest
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.gateway.gateway.boundaries.GroupBoundaryCallback
import com.doneit.ascend.domain.use_case.gateway.IGroupGateway
import com.doneit.ascend.source.storage.remote.data.request.group.GroupSocketCookies
import com.doneit.ascend.source.storage.remote.repository.group.IGroupRepository
import com.doneit.ascend.source.storage.remote.repository.group.socket.IGroupSocketRepository
import com.vrgsoft.networkmanager.NetworkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.Executors

internal class GroupGateway(
    errors: NetworkManager,
    private val groupLocal: com.doneit.ascend.source.storage.local.repository.groups.IGroupRepository,
    private val remote: IGroupRepository,
    private val remoteSocket: IGroupSocketRepository,
    private val accountManager: AccountManager,//todo move logic with account manager to corresponding Repository
    private val packageName: String
) : BaseGateway(errors), IGroupGateway {

    override fun <T> calculateMessage(error: T): String {
        return ""//todo, not required for now
    }

    override val messagesStream =
        remoteSocket.messagesStream.map { it.toEntity() }//todo remove deprecation

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
        val res = remote.getGroupsList(groupListModel.toRequest()).toResponseEntity(
            {
                it?.groups?.map { it.toEntity() }
            },
            {
                it?.errors
            }
        )

        if (res.isSuccessful) {
            groupLocal.removeAll()
            groupLocal.insertAll(res.successModel!!.map { it.toLocal() })
        }

        return res
    }

    override fun getGroupsListPaged(listRequest: GroupListModel): LiveData<PagedList<GroupEntity>> =
        liveData {
            groupLocal.removeAll()

            val config = getConfigPaged(listRequest)
            val factory = groupLocal.getGroupList(listRequest.toLocal()).map { it.toEntity() }

            val boundary = GroupBoundaryCallback(
                GlobalScope,
                groupLocal,
                remote,
                listRequest
            )

            emitSource(
                LivePagedListBuilder<Int, GroupEntity>(factory, config)
                    .setFetchExecutor(Executors.newSingleThreadExecutor())
                    .setBoundaryCallback(boundary)
                    .build()
            )

            boundary.loadInitial()
        }

    override suspend fun getGroupDetails(groupId: Long): ResponseEntity<GroupEntity, List<String>> {
        val localGroup = groupLocal.getGroupById(groupId)
        if (localGroup != null) {

            return ResponseEntity(
                true,
                -1,
                "",
                localGroup.toEntity(),
                null
            )
        } else {
            val res = executeRemote { remote.getGroupDetails(groupId) }.toResponseEntity(
                {
                    it?.toEntity()
                },
                {
                    it?.errors
                }
            )

            if(res.isSuccessful) {
                GlobalScope.launch(Dispatchers.IO) {
                    groupLocal.insertAll(listOf(res.successModel!!.toLocal()))
                }
            }

            return res
        }
    }

    override fun getGroupDetailsLive(groupId: Long) = liveData<GroupEntity?> {
        emitSource(groupLocal.getGroupByIdLive(groupId).map { it?.toEntity() })

        val result = executeRemote { remote.getGroupDetails(groupId) }.toResponseEntity(
            {
                it?.toEntity()
            },
            {
                it?.errors
            }
        )

        if(result.isSuccessful) {
            groupLocal.insertAll(listOf(result.successModel!!.toLocal()))
        }
    }

    override fun updateGroupLocal(group: GroupEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            groupLocal.update(group.toLocal())
        }
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
        val res = remote.subscribe(model.groupId, model.toRequest()).toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )

        if (res.isSuccessful) {
            val group = groupLocal.getGroupById(model.groupId)
            group?.let {

                groupLocal.update(
                    it.copy(
                        subscribed = true
                    )
                )
            }
        }

        return res
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

    override suspend fun getParticipantList(listModel: ParticipantListModel): ResponseEntity<List<ParticipantEntity>, List<String>> {
        return remote.getParticipants(listModel.groupId, listModel.toRequest()).toResponseEntity(
            {
                it?.participants?.map { it.toEntity() }
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
                val cookies =
                    GroupSocketCookies(
                        token,
                        groupId
                    )

                remoteSocket.connect(cookies)
            }
        }
    }

    override fun sendSocketMessage(message: String) {
        remoteSocket.sendMessage(message)
    }

    override fun disconnect() {
        remoteSocket.disconnect()
    }

    private fun getConfigPaged(model: BasePagedModel): PagedList.Config {
        return PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(model.perPage ?: 10)
            .build()
    }
}