package com.doneit.ascend.domain.gateway.gateway

import android.accounts.AccountManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.ParticipantEntity
import com.doneit.ascend.domain.entity.TagEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.*
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_locale.toLocal
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toCreateGroupRequest
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toUpdateGroupRequest
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.gateway.gateway.boundaries.GroupBoundaryCallback
import com.doneit.ascend.domain.gateway.gateway.data_source.UserDataSourceFactory
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

    override suspend fun createGroup(groupDTO: CreateGroupDTO): ResponseEntity<GroupEntity, List<String>> {
        return executeRemote {
            remote.createGroup(
                File(groupDTO.imagePath),
                groupDTO.toCreateGroupRequest()
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

    override suspend fun updateGroup(
        id: Long,
        groupDTO: UpdateGroupDTO
    ): ResponseEntity<GroupEntity, List<String>> {
        return executeRemote {
            remote.updateGroup(
                id,
                groupDTO.imagePath.let {
                    if (it != null) {
                        File(it)
                    } else {
                        null
                    }
                },
                groupDTO.toUpdateGroupRequest()
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

    override suspend fun getGroupsList(groupListModel: GroupListDTO): ResponseEntity<List<GroupEntity>, List<String>> {
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

    override fun getGroupsListPaged(listRequest: GroupListDTO): LiveData<PagedList<GroupEntity>> =
        liveData {
            groupLocal.removeAll()

            val config = getDefaultPagedConfig(listRequest)
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
        val res = executeRemote { remote.getGroupDetails(groupId) }.toResponseEntity(
            {
                it?.toEntity()
            },
            {
                it?.errors
            }
        )

        if (res.isSuccessful) {
            GlobalScope.launch(Dispatchers.IO) {
                groupLocal.insertAll(listOf(res.successModel!!.toLocal()))
            }
        }

        return res
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

        if (result.isSuccessful) {
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

    override suspend fun leaveGroup(groupId: Long): ResponseEntity<Unit, List<String>> {
        return executeRemote { remote.leaveGroup(groupId) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun deleteInvite(
        groupId: Long,
        inviteId: Long
    ): ResponseEntity<Unit, List<String>> {
        return executeRemote { remote.deleteInvite(groupId, inviteId) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun updateNote(dto: UpdateNoteDTO): ResponseEntity<Unit, List<String>> {
        return executeRemote { remote.updateNote(dto.groupId, dto.toRequest()) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun cancelGroup(dto: CancelGroupDTO): ResponseEntity<Unit, List<String>> {
        return executeRemote { remote.cancelGroup(dto.groupId, dto.toRequest()) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun inviteToGroup(dto: InviteToGroupDTO): ResponseEntity<Unit, List<String>> {
        return executeRemote {
            remote.inviteToGroup(
                dto.groupId,
                dto.toRequest()
            )
        }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }

    override fun getMembersPaged(
        query: String,
        userId: Long,
        memberList: List<AttendeeEntity>?
    ): LiveData<PagedList<AttendeeEntity>> {
        return UserDataSourceFactory(GlobalScope, remote, query, userId, memberList).toLiveData(
            pageSize = 10,
            fetchExecutor = Executors.newSingleThreadExecutor()
        )
    }


    override suspend fun subscribe(dto: SubscribeGroupDTO): ResponseEntity<Unit, List<String>> {
        val res = remote.subscribe(dto.groupId, dto.toRequest()).toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )

        if (res.isSuccessful) {
            val group = groupLocal.getGroupById(dto.groupId)
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

    override suspend fun getCredentials(groupId: Long): ResponseEntity<GroupCredentialsDTO, List<String>> {
        return remote.getCredentials(groupId).toResponseEntity(
            {
                it?.toEntity()
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun getParticipantList(listModel: ParticipantListDTO): ResponseEntity<List<ParticipantEntity>, List<String>> {
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

    override suspend fun getTags(): ResponseEntity<List<TagEntity>, List<String>> {
        return executeRemote { remote.getTags()}.toResponseEntity(
            {
                it?.tags?.map { it.toEntity() }
            },
            {
                it?.errors
            }
        )
    }
}