package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.entity.dto.*
import com.doneit.ascend.domain.gateway.common.applyDaysOffset
import com.doneit.ascend.domain.gateway.common.getDayOffset
import com.doneit.ascend.source.storage.remote.data.request.SubscribeGroupRequest
import com.doneit.ascend.source.storage.remote.data.request.group.*

fun CreateGroupDTO.toCreateGroupRequest(): CreateGroupRequest {
    val dayOffset = startTime?.getDayOffset()?: 0

    return CreateGroupRequest(
        name,
        description,
        startTime?.toRemoteString(),
        groupType,
        price?.toPrice(),
        participants,
        days?.applyDaysOffset(dayOffset),
        meetingsCount,
        meetingFormat,
        privacy,
        tags
    )
}
fun UpdateGroupDTO.toUpdateGroupRequest(): UpdateGroupRequest {
    val dayOffset = startTime?.getDayOffset()?: 0

    return UpdateGroupRequest(
        name,
        description,
        startTime?.toRemoteString(),
        groupType,
        price?.toPrice(),
        participants,
        participantsToDelete,
        days?.applyDaysOffset(dayOffset),
        meetingsCount,
        meetingFormat,
        privacy,
        tags
    )
}

fun Float.toPrice(): Int {
    return (this * 100).toInt()
}

fun GroupListDTO.toRequest(): GroupListRequest {
    return GroupListRequest(
        page,
        perPage,
        sortColumn,
        sortType?.toString(),
        name,
        userId,
        groupType?.toString(),
        groupStatus.toString(),
        myGroups,
        startDateFrom?.toRemoteString(),
        startDateTo?.toRemoteString()
    )
}

fun GroupListDTO.toRequest(currPage: Int): GroupListRequest {
    return GroupListRequest(
        currPage,
        perPage,
        sortColumn,
        sortType?.toString(),
        name,
        userId,
        groupType?.toString(),
        groupStatus.toString(),
        myGroups,
        startDateFrom?.toRemoteString(),
        startDateTo?.toRemoteString()
    )
}

fun SubscribeGroupDTO.toRequest(): SubscribeGroupRequest {
    return SubscribeGroupRequest(
        paymentSourceId,
        paymentSourceType.toString()
    )
}

fun ParticipantListDTO.toRequest(): GroupParticipantsRequest {
    return GroupParticipantsRequest(
        page,
        perPage,
        sortColumn,
        sortType?.toString(),
        fullName,
        connected
    )
}

fun UpdateNoteDTO.toRequest(): UpdateNoteRequest {
    return UpdateNoteRequest(
        content
    )
}

fun CancelGroupDTO.toRequest(): CancelGroupRequest {
    return CancelGroupRequest(
        reason
    )
}

fun InviteToGroupDTO.toRequest(): InviteToGroupRequest {
    return InviteToGroupRequest(
        participants
    )
}