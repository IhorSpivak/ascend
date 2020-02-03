package com.doneit.ascend.domain.use_case.gateway

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.ParticipantEntity
import com.doneit.ascend.domain.entity.SocketEventEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.*

interface IGroupGateway {
    suspend fun createGroup(groupModel: CreateGroupModel): ResponseEntity<GroupEntity, List<String>>

    suspend fun getGroupsList(groupListModel: GroupListModel): ResponseEntity<List<GroupEntity>, List<String>>

    fun getGroupsListPaged(listRequest: GroupListModel): LiveData<PagedList<GroupEntity>>

    suspend fun getGroupDetails(groupId: Long): ResponseEntity<GroupEntity, List<String>>

    fun updateGroupLocal(group: GroupEntity)

    suspend fun deleteGroup(groupId: Long): ResponseEntity<Unit, List<String>>

    suspend fun subscribe(model: SubscribeGroupModel): ResponseEntity<Unit, List<String>>

    suspend fun getCredentials(groupId: Long): ResponseEntity<GroupCredentialsModel, List<String>>

    suspend fun getParticipantList(listModel: ParticipantListModel): ResponseEntity<List<ParticipantEntity>, List<String>>

    val messagesStream: LiveData<SocketEventEntity>

    fun connectToChannel(groupId: Long)

    fun sendSocketMessage(message: String)

    fun disconnect()
}