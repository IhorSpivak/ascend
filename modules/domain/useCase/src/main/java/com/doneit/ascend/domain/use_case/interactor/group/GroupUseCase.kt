package com.doneit.ascend.domain.use_case.interactor.group

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.ParticipantEntity
import com.doneit.ascend.domain.entity.SocketEventEntity
import com.doneit.ascend.domain.entity.TagEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.*
import com.doneit.ascend.domain.entity.group.GroupEntity

interface GroupUseCase {
    suspend fun createGroup(groupDTO: CreateGroupDTO): ResponseEntity<GroupEntity, List<String>>

    suspend fun updateGroup(id : Long, groupDTO: UpdateGroupDTO): ResponseEntity<GroupEntity, List<String>>

    suspend fun getGroupList(model: GroupListDTO): ResponseEntity<List<GroupEntity>, List<String>>

    fun getGroupListPaged(model: GroupListDTO): LiveData<PagedList<GroupEntity>>

    suspend fun getGroupDetails(groupId: Long): ResponseEntity<GroupEntity, List<String>>

    fun getGroupDetailsLive(groupId: Long): LiveData<GroupEntity?>

    fun updateGroupLocal(group: GroupEntity)

    suspend fun deleteGroup(groupId: Long): ResponseEntity<Unit, List<String>>

    suspend fun leaveGroup(groupId: Long): ResponseEntity<Unit, List<String>>

    suspend fun deleteInvite(groupId: Long, inviteId: Long): ResponseEntity<Unit, List<String>>

    suspend fun subscribe(dto: SubscribeGroupDTO): ResponseEntity<Unit, List<String>>

    suspend fun getCredentials(groupId: Long): ResponseEntity<GroupCredentialsDTO, List<String>>

    suspend fun getWebinarCredentials(groupId: Long): ResponseEntity<WebinarCredentialsDTO, List<String>>

    suspend fun getParticipantList(groupId: Long, fullName: String? = null, isConnected: Boolean? = null): ResponseEntity<List<ParticipantEntity>, List<String>>

    suspend fun updateNote(dto: UpdateNoteDTO): ResponseEntity<Unit, List<String>>

    suspend fun cancelGroup(dto: CancelGroupDTO): ResponseEntity<Unit, List<String>>

    suspend fun inviteToGroup(dto: InviteToGroupDTO): ResponseEntity<Unit, List<String>>

    suspend fun getTags(): ResponseEntity<List<TagEntity>, List<String>>

    fun searchMembers(query: String, userId: Long, memberList: List<AttendeeEntity>? = null): LiveData<PagedList<AttendeeEntity>>

    val messagesStream: LiveData<SocketEventEntity>

    fun connectToChannel(groupId: Long)

    fun startGroup()

    fun riseOwnHand()

    fun lowerOwnHand()

    fun lowerAHand(userId: String)

    fun allowToSay(userId: String)

    fun removeChatParticipant(userId: String)

    fun muteUser(userId: String)

    fun unmuteUser(userId: String)

    fun muteAllUsers(userId: String)

    fun unMuteAllUsers(userId: String)

    fun disconnect()
}