package com.doneit.ascend.domain.use_case.interactor.group

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.ParticipantEntity
import com.doneit.ascend.domain.entity.SocketEventEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.*

interface GroupUseCase {
    suspend fun createGroup(groupModel: CreateGroupModel): ResponseEntity<GroupEntity, List<String>>

    suspend fun getGroupList(model: GroupListModel): ResponseEntity<List<GroupEntity>, List<String>>

    suspend fun getGroupListPaged(model: GroupListModel): PagedList<GroupEntity>

    suspend fun getGroupDetails(groupId: Long): ResponseEntity<GroupEntity, List<String>>

    suspend fun deleteGroup(groupId: Long): ResponseEntity<Unit, List<String>>

    suspend fun subscribe(model: SubscribeGroupModel): ResponseEntity<Unit, List<String>>

    suspend fun getCredentials(groupId: Long): ResponseEntity<GroupCredentialsModel, List<String>>

    suspend fun getParticipantList(groupId: Long, fullName: String? = null, isConnected: Boolean? = null): ResponseEntity<List<ParticipantEntity>, List<String>>

    val messagesStream: LiveData<SocketEventEntity>

    fun connectToChannel(groupId: Long)

    fun riseOwnHand()

    fun lowerOwnHand()

    fun lowerAHand(userId: Long)

    fun allowToSay(userId: Long)

    fun removeChatParticipant(userId: Long)

    fun disconnect()
}