package com.doneit.ascend.domain.use_case.interactor.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.ParticipantEntity
import com.doneit.ascend.domain.entity.SocketEvent
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.*
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.use_case.gateway.IGroupGateway

internal class GroupInteractor(
    private val groupGateway: IGroupGateway
) : GroupUseCase {

    override suspend fun createGroup(groupDTO: CreateGroupDTO): ResponseEntity<GroupEntity, List<String>> {
        return groupGateway.createGroup(groupDTO)
    }

    override suspend fun updateGroup(id: Long, groupDTO: CreateGroupDTO): ResponseEntity<GroupEntity, List<String>> {
        return groupGateway.updateGroup(id, groupDTO)
    }

    override suspend fun getGroupList(model: GroupListDTO): ResponseEntity<List<GroupEntity>, List<String>> {
        return groupGateway.getGroupsList(model)
    }

    override fun getGroupListPaged(model: GroupListDTO): LiveData<PagedList<GroupEntity>> {
        return groupGateway.getGroupsListPaged(model)
    }

    override suspend fun getGroupDetails(groupId: Long): ResponseEntity<GroupEntity, List<String>> {
        return groupGateway.getGroupDetails(groupId)
    }

    override fun getGroupDetailsLive(groupId: Long): LiveData<GroupEntity?> {
        return groupGateway.getGroupDetailsLive(groupId)
    }

    override fun updateGroupLocal(group: GroupEntity) {
        return groupGateway.updateGroupLocal(group)
    }

    override suspend fun deleteGroup(groupId: Long): ResponseEntity<Unit, List<String>> {
        return groupGateway.deleteGroup(groupId)
    }

    override suspend fun subscribe(dto: SubscribeGroupDTO): ResponseEntity<Unit, List<String>> {
        return groupGateway.subscribe(dto)
    }

    override suspend fun getCredentials(groupId: Long): ResponseEntity<GroupCredentialsDTO, List<String>> {
        return groupGateway.getCredentials(groupId)
    }

    override suspend fun getParticipantList(
        groupId: Long,
        fullName: String?,
        isConnected: Boolean?
    ): ResponseEntity<List<ParticipantEntity>, List<String>> {
        return groupGateway.getParticipantList(
            ParticipantListDTO(
                1,
                CHAT_PARTICIPANTS_MAX_COUNT,
                null,
                null,
                fullName,
                isConnected,
                groupId
            )
        )
    }

    override suspend fun updateNote(dto: UpdateNoteDTO): ResponseEntity<Unit, List<String>> {
        return groupGateway.updateNote(dto)
    }

    override fun searchMembers(query: String): LiveData<PagedList<AttendeeEntity>> {
        return groupGateway.getMembersPaged(query)
    }

    override val messagesStream = groupGateway.messagesStream

    override fun startGroup() {
        groupGateway.sendSocketMessage(String.format(EVENT_TEMPLATE, SocketEvent.GROUP_STARTED.toString()))
    }

    override fun connectToChannel(groupId: Long) {
        groupGateway.connectToChannel(groupId)
    }

    override fun riseOwnHand() {
        groupGateway.sendSocketMessage(String.format(EVENT_TEMPLATE, SocketEvent.RISE_A_HAND.toString()))
    }

    override fun lowerOwnHand() {
        groupGateway.sendSocketMessage(String.format(EVENT_TEMPLATE, SocketEvent.REMOVE_HAND.toString()))
    }

    override fun lowerAHand(userId: String) {
        groupGateway.sendSocketMessage(
            String.format(
                EVENT_WITH_ID_TEMPLATE,
                SocketEvent.REMOVE_HAND.toString(),
                userId
            )
        )
    }

    override fun allowToSay(userId: String) {
        groupGateway.sendSocketMessage(String.format(EVENT_WITH_ID_TEMPLATE,
            SocketEvent.SPEAK.toString(), userId))
    }

    override fun removeChatParticipant(userId: String) {
        groupGateway.sendSocketMessage(
            String.format(
                EVENT_WITH_ID_TEMPLATE,
                SocketEvent.REMOVED_FROM_GROUP.toString(),
                userId
            )
        )
    }

    override fun muteUser(userId: String) {
        groupGateway.sendSocketMessage(
            String.format(
                EVENT_WITH_ID_TEMPLATE,
                SocketEvent.MUTE_USER.toString(),
                userId
            )
        )
    }

    override fun unmuteUser(userId: String) {
        groupGateway.sendSocketMessage(
            String.format(
                EVENT_WITH_ID_TEMPLATE,
                SocketEvent.RESET_MUTE_USER.toString(),
                userId
            )
        )
    }

    override fun disconnect() {
        groupGateway.disconnect()
    }

    companion object {
        private const val EVENT_TEMPLATE =
            "{\"command\":\"message\",\"data\":\"{\\\"event\\\":\\\"%s\\\",\\\"action\\\":\\\"speak\\\"}\",\"identifier\":\"{\\\"channel\\\":\\\"GroupChannel\\\"}\"}"

        private const val EVENT_WITH_ID_TEMPLATE =
            "{\"command\":\"message\",\"data\":\"{\\\"event\\\":\\\"%s\\\",\\\"action\\\":\\\"speak\\\",\\\"user_id\\\":\\\"%s\\\"}\",\"identifier\":\"{\\\"channel\\\":\\\"GroupChannel\\\"}\"}"

        private const val CHAT_PARTICIPANTS_MAX_COUNT = 100
    }
}