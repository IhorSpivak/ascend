package com.doneit.ascend.domain.use_case.interactor.group

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.ParticipantEntity
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

    override val messagesStream = groupGateway.messagesStream

    override fun startGroup() {
        groupGateway.sendSocketMessage(START_GROUP)
    }

    override fun connectToChannel(groupId: Long) {
        groupGateway.connectToChannel(groupId)
    }

    override fun riseOwnHand() {
        groupGateway.sendSocketMessage(RISE_A_HAND_MESSAGE)
    }

    override fun lowerOwnHand() {
        groupGateway.sendSocketMessage(LOWER_OWN_HAND)
    }

    override fun lowerAHand(userId: String) {
        groupGateway.sendSocketMessage(String.format(LOWER_A_HAND, userId))
    }

    override fun allowToSay(userId: String) {
        groupGateway.sendSocketMessage(String.format(ALLOW_TO_SAY, userId))
    }

    override fun removeChatParticipant(userId: String) {
        groupGateway.sendSocketMessage(String.format(REMOVE_PARTICIPANT, userId))
    }

    override fun muteUser(userId: Long) {
        //TODO:
        //groupGateway.sendSocketMessage(String.format())
    }

    override fun disconnect() {
        groupGateway.disconnect()
    }

    companion object {
        private const val RISE_A_HAND_MESSAGE =
            "{\"command\":\"message\",\"data\":\"{\\\"event\\\":\\\"RiseAHand\\\",\\\"action\\\":\\\"speak\\\"}\",\"identifier\":\"{\\\"channel\\\":\\\"GroupChannel\\\"}\"}"
        private const val LOWER_OWN_HAND =
            "{\"command\":\"message\",\"data\":\"{\\\"event\\\":\\\"RemoveHand\\\",\\\"action\\\":\\\"speak\\\"}\",\"identifier\":\"{\\\"channel\\\":\\\"GroupChannel\\\"}\"}"
        private const val LOWER_A_HAND =
            "{\"command\":\"message\",\"data\":\"{\\\"event\\\":\\\"RemoveHand\\\",\\\"action\\\":\\\"speak\\\",\\\"user_id\\\":\\\"%s\\\"}\",\"identifier\":\"{\\\"channel\\\":\\\"GroupChannel\\\"}\"}"
        private const val ALLOW_TO_SAY =
            "{\"command\":\"message\",\"data\":\"{\\\"event\\\":\\\"Speak\\\",\\\"action\\\":\\\"speak\\\",\\\"user_id\\\":\\\"%s\\\"}\",\"identifier\":\"{\\\"channel\\\":\\\"GroupChannel\\\"}\"}"
        private const val REMOVE_PARTICIPANT =
            "{\"command\":\"message\",\"data\":\"{\\\"event\\\":\\\"RemoveParticipant\\\",\\\"action\\\":\\\"speak\\\",\\\"user_id\\\":\\\"%s\\\"}\",\"identifier\":\"{\\\"channel\\\":\\\"GroupChannel\\\"}\"}"
        private const val START_GROUP =
            "{\"command\":\"message\",\"data\":\"{\\\"event\\\":\\\"StartGroup\\\",\\\"action\\\":\\\"speak\\\"}\",\"identifier\":\"{\\\"channel\\\":\\\"GroupChannel\\\"}\"}"

        private const val CHAT_PARTICIPANTS_MAX_COUNT = 100
    }
}