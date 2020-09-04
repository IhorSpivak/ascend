package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.entity.dto.*
import com.doneit.ascend.domain.gateway.common.applyDaysOffset
import com.doneit.ascend.domain.gateway.common.getDayOffset
import com.doneit.ascend.source.storage.remote.data.request.SubscribeGroupRequest
import com.doneit.ascend.source.storage.remote.data.request.group.*
import java.text.SimpleDateFormat
import java.util.*

fun CreateGroupDTO.toCreateGroupRequest(): CreateGroupRequest {
    val dayOffset = startTime?.getDayOffset()?: 0

    return CreateGroupRequest(
        name = name,
        description = description,
        startTime = startTime?.toRemoteString(),
        groupType = groupType,
        price = price?.toPrice(),
        participants = participants,
        days = days?.applyDaysOffset(dayOffset),
        meetingsCount = meetingsCount,
        meetingFormat = meetingFormat,
        private = privacy,
        tagId = tags,
        times = dates,
        themes = themes,
        duration = duration
    )
}
fun UpdateGroupDTO.toUpdateGroupRequest(): UpdateGroupRequest {
    val dayOffset = startTime?.getDayOffset()?: 0

    return UpdateGroupRequest(
        name = name,
        description = description,
        startTime = startTime?.toRemoteString(),
        groupType = groupType,
        price = price?.toPrice(),
        participants = participants,
        removedParticipants = participantsToDelete,
        days = days?.applyDaysOffset(dayOffset),
        meetingsCount = meetingsCount,
        meetingFormat = meetingFormat,
        private = privacy,
        tagId = tags,
        times = times,
        themes = themes,
        duration = duration
    )
}

fun Float.toPrice(): Int {
    return (this * 100).toInt()
}

fun GroupListDTO.toRequest(): GroupListRequest {
    return GroupListRequest(
        page = page,
        perPage = perPage,
        sortColumn = sortColumn,
        sortType = sortType?.toString(),
        name = name,
        userId = userId,
        groupType = groupType?.toString(),
        status = groupStatus.toString(),
        myGroups = myGroups,
        startTimeFrom = startDateFrom?.toRemoteString(),
        startTimeTo = startDateTo?.toRemoteString(),
        timeFrom = timeFrom?.toGMTTimestamp(),
        timeTo = timeTo?.toGMTTimestamp(),
        community = community,
        tagId = tagId,
        days = daysOfWeen.orEmpty()
    )
}

fun GroupListDTO.toRequest(currPage: Int): GroupListRequest {
    return GroupListRequest(
        page = currPage,
        perPage = perPage,
        sortColumn = sortColumn,
        sortType = sortType?.toString(),
        name = name,
        userId = userId,
        groupType = groupType?.toString(),
        status = groupStatus.toString(),
        myGroups = myGroups,
        startTimeFrom = startDateFrom?.toRemoteString(),
        startTimeTo = startDateTo?.toRemoteString(),
        timeFrom = timeFrom?.toGMTTimestamp(),
        timeTo = timeTo?.toGMTTimestamp(),
        community = community,
        tagId = tagId,
        days = daysOfWeen.orEmpty()
    )
}

fun SubscribeGroupDTO.toRequest(): SubscribeGroupRequest {
    return SubscribeGroupRequest(
        paymentSourceId = paymentSourceId,
        paymentSourceType = paymentSourceType?.toString()
    )
}

fun ParticipantListDTO.toRequest(): GroupParticipantsRequest {
    return GroupParticipantsRequest(
        page = page,
        perPage = perPage,
        sortColumn = sortColumn,
        sortType = sortType?.toString(),
        fullName = fullName,
        connected = connected
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
fun getTimeFormat(): SimpleDateFormat{
    return SimpleDateFormat("HH:mm", Locale.ENGLISH)
}