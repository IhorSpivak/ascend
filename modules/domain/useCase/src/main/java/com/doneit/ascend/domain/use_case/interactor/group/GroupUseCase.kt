package com.doneit.ascend.domain.use_case.interactor.group

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.ParticipantEntity
import com.doneit.ascend.domain.entity.SocketEventEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.*
import com.doneit.ascend.domain.entity.group.GroupEntity

interface GroupUseCase {
    suspend fun createGroup(groupDTO: CreateGroupDTO): ResponseEntity<GroupEntity, List<String>>

    suspend fun getGroupList(model: GroupListDTO): ResponseEntity<List<GroupEntity>, List<String>>

    fun getGroupListPaged(model: GroupListDTO): LiveData<PagedList<GroupEntity>>

    suspend fun getGroupDetails(groupId: Long): ResponseEntity<GroupEntity, List<String>>

    fun getGroupDetailsLive(groupId: Long): LiveData<GroupEntity?>

    fun updateGroupLocal(group: GroupEntity)

    suspend fun deleteGroup(groupId: Long): ResponseEntity<Unit, List<String>>

    suspend fun subscribe(dto: SubscribeGroupDTO): ResponseEntity<Unit, List<String>>

    suspend fun getCredentials(groupId: Long): ResponseEntity<GroupCredentialsDTO, List<String>>

    suspend fun getParticipantList(groupId: Long, fullName: String? = null, isConnected: Boolean? = null): ResponseEntity<List<ParticipantEntity>, List<String>>

    suspend fun updateNote(dto: UpdateNoteDTO): ResponseEntity<Unit, List<String>>

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

    fun disconnect()
}