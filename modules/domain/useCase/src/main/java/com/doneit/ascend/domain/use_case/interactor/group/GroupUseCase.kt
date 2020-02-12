package com.doneit.ascend.domain.use_case.interactor.group

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.ParticipantEntity
import com.doneit.ascend.domain.entity.SocketEventEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.*
import com.doneit.ascend.domain.entity.group.GroupEntity

interface GroupUseCase {
    suspend fun createGroup(groupModel: CreateGroupModel): ResponseEntity<GroupEntity, List<String>>

    suspend fun getGroupList(model: GroupListModel): ResponseEntity<List<GroupEntity>, List<String>>

    fun getGroupListPaged(model: GroupListModel): LiveData<PagedList<GroupEntity>>

    suspend fun getGroupDetails(groupId: Long): ResponseEntity<GroupEntity, List<String>>

    fun getGroupDetailsLive(groupId: Long): LiveData<GroupEntity?>

    fun updateGroupLocal(group: GroupEntity)

    suspend fun deleteGroup(groupId: Long): ResponseEntity<Unit, List<String>>

    suspend fun subscribe(model: SubscribeGroupModel): ResponseEntity<Unit, List<String>>

    suspend fun getCredentials(groupId: Long): ResponseEntity<GroupCredentialsModel, List<String>>

    suspend fun getParticipantList(groupId: Long, fullName: String? = null, isConnected: Boolean? = null): ResponseEntity<List<ParticipantEntity>, List<String>>

    suspend fun updateNote(dto: UpdateNoteDTO): ResponseEntity<Unit, List<String>>

    val messagesStream: LiveData<SocketEventEntity>

    fun connectToChannel(groupId: Long)

    fun startGroup()

    fun riseOwnHand()

    fun lowerOwnHand()

    fun lowerAHand(userId: Long)

    fun allowToSay(userId: Long)

    fun removeChatParticipant(userId: Long)

    fun muteUser(userId: Long)

    fun disconnect()
}