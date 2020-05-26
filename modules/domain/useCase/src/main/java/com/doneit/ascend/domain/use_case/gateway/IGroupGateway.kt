package com.doneit.ascend.domain.use_case.gateway

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.ParticipantEntity
import com.doneit.ascend.domain.entity.SocketEventEntity
import com.doneit.ascend.domain.entity.TagEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.*
import com.doneit.ascend.domain.entity.group.GroupEntity

interface IGroupGateway {
    suspend fun createGroup(groupDTO: CreateGroupDTO, credentialsDTO: GroupCredentialsDTO? = null): ResponseEntity<GroupEntity, List<String>>

    suspend fun updateGroup(id: Long, groupDTO: UpdateGroupDTO): ResponseEntity<GroupEntity, List<String>>

    suspend fun getGroupsList(groupListModel: GroupListDTO): ResponseEntity<List<GroupEntity>, List<String>>

    fun getGroupsListPaged(listRequest: GroupListDTO): LiveData<PagedList<GroupEntity>>

    suspend fun getGroupDetails(groupId: Long): ResponseEntity<GroupEntity, List<String>>

    fun getGroupDetailsLive(groupId: Long): LiveData<GroupEntity?>

    fun updateGroupLocal(group: GroupEntity)

    suspend fun deleteGroup(groupId: Long): ResponseEntity<Unit, List<String>>

    suspend fun leaveGroup(groupId: Long): ResponseEntity<Unit, List<String>>

    suspend fun deleteInvite(groupId: Long, inviteId: Long): ResponseEntity<Unit, List<String>>

    suspend fun subscribe(dto: SubscribeGroupDTO): ResponseEntity<Unit, List<String>>

    suspend fun getCredentials(groupId: Long): ResponseEntity<GroupCredentialsDTO, List<String>>

    suspend fun getWebinarCredentials(groupId: Long): ResponseEntity<WebinarCredentialsDTO, List<String>>

    suspend fun getParticipantList(listModel: ParticipantListDTO): ResponseEntity<List<ParticipantEntity>, List<String>>

    suspend fun updateNote(dto: UpdateNoteDTO): ResponseEntity<Unit, List<String>>

    suspend fun cancelGroup(dto: CancelGroupDTO): ResponseEntity<Unit, List<String>>

    suspend fun inviteToGroup(dto: InviteToGroupDTO): ResponseEntity<Unit, List<String>>

    fun getMembersPaged(query: String, userId: Long, memberList: List<AttendeeEntity>?): LiveData<PagedList<AttendeeEntity>>

    val messagesStream: LiveData<SocketEventEntity>

    fun connectToChannel(groupId: Long)

    fun sendSocketMessage(message: String)

    fun disconnect()

    suspend fun getTags() : ResponseEntity<List<TagEntity>, List<String>>
}