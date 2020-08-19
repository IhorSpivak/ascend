package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.*
import com.doneit.ascend.domain.entity.group.*
import com.doneit.ascend.domain.entity.webinar_question.QuestionSocketEntity
import com.doneit.ascend.domain.entity.webinar_question.QuestionSocketEvent
import com.doneit.ascend.domain.gateway.common.applyDaysOffset
import com.doneit.ascend.domain.gateway.common.getDayOffset
import com.doneit.ascend.source.storage.local.data.*
import com.doneit.ascend.source.storage.remote.data.response.ImageResponse
import com.doneit.ascend.source.storage.remote.data.response.OwnerResponse
import com.doneit.ascend.source.storage.remote.data.response.TagResponse
import com.doneit.ascend.source.storage.remote.data.response.ThumbnailResponse
import com.doneit.ascend.source.storage.remote.data.response.chat.ChatSocketEventMessage
import com.doneit.ascend.source.storage.remote.data.response.group.*

fun ThumbnailResponse.toEntity(): ThumbnailEntity {
    return ThumbnailEntity(
        url = url
    )
}

fun ImageResponse.toEntity(): ImageEntity {
    return ImageEntity(
        url = url,
        thumbnail = thumbnail?.toEntity()
    )
}

fun OwnerResponse.toEntity(): OwnerEntity {
    return OwnerEntity(
        id = id,
        fullName = fullName.orEmpty(),
        image = image?.toEntity(),
        rating = rating ?: 0.0f,
        followed = followed ?: false,
        location = location.orEmpty(),
        connected = connected ?: false
    )
}

fun TagResponse.toEntity(): TagEntity {
    return TagEntity(
        id = id,
        tag = tag
    )
}

fun BannerResponse.toEntity() = Banner(
    title = title.orEmpty(),
    bannerType = bannerType.orEmpty()
)

fun GroupResponse.toEntity(): GroupEntity {
    val startT = startTime.toDate()!!
    val dayOffset = -1 * startT.getDayOffset()

    return GroupEntity(
        id = id,
        name = name,
        description = description,
        startTime = startT,
        status = status.toGroupStatus(),
        groupType = groupType.toGroupType(),
        price = price / 100,
        image = image.toEntity(),
        meetingsCount = meetingsCount,
        passedCount = 0,//todo map from server
        createdAt = createdAt.toDate(),
        updatedAt = updatedAt.toDate(),
        banner = banner?.toEntity(),
        owner = owner.toEntity(),
        subscribed = subscribed,
        invited = invited,
        blocked = blocked,
        participantsCount = participantsCount,
        invitesCount = invitesCount,
        daysOfWeek = getDays(daysOfWeek, dayOffset),
        note = note?.toEntity(),
        meetingFormat = meetingFormat,
        tag = tag?.toEntity(),
        attendees = invites?.map { it.toEntity() },
        isPrivate = private,
        pastMeetingsCount = pastMeetingsCount,
        dates = dates,
        themes = themes,
        duration = duration,
        community = community
    )
}

private fun getDays(list: List<Int>?, dayOffset: Int): List<CalendarDayEntity> {
    return list?.applyDaysOffset(dayOffset)?.map { it.toCalendarDay() }
        ?: listOf(CalendarDayEntity.SUNDAY)
}

fun NoteResponse.toEntity(): NoteEntity {
    return NoteEntity(
        content = content,
        updatedAt = updatedAt.toDate()!!
    )
}

fun String.toGroupStatus(): GroupStatus? {
    return GroupStatus.valueOf(this.toUpperCase())
}

fun String.toGroupType(): GroupType? {
    return GroupType.valueOf(this.toUpperCase())
}

fun GroupCredentialsResponse.toEntity(): GroupCredentialsEntity {
    return GroupCredentialsEntity(
        name = name,
        token = token
    )
}

fun WebinarCredentialsResponse.toEntity(): WebinarCredentialsEntity {
    return WebinarCredentialsEntity(
        key = key,
        link = link,
        chatId = chatId
    )
}

fun SocketEventMessage.toEntity(): SocketEventEntity {
    val event = SocketEvent.fromRemoteString(event!!)
    return SocketEventEntity(
        event,
        SocketUserEntity(
            userId = userId,
            fullName = fullName,
            image = image?.toEntity(),
            isHandRisen = event == SocketEvent.RISE_A_HAND
        )
    )
}

fun QuestionSocketEventMessage.toEntity(): QuestionSocketEntity {
    val event = QuestionSocketEvent.fromRemoteString(event!!)
    return QuestionSocketEntity(
        id = id,
        question = question,
        userId = userId,
        createdAt = createdAt,
        updatedAt = updatedAt,
        fullName = fullName,
        image = image?.toEntity(),
        event = event
    )
}

private fun Int.toCalendarDay(): CalendarDayEntity {
    return CalendarDayEntity.values()[this]
}

fun ParticipantResponse.toEntity(): ParticipantEntity {
    return ParticipantEntity(
        id = id,
        email = email,
        fullName = fullName,
        image = image?.toEntity(),
        isHandRisen = isHandRisen,
        isConnected = isConnected,
        isVisited = isVisited,
        isBlocked = isBlocked,
        isSpeaker = isSpeaker,
        isMuted = isMuted
    )
}

fun GroupLocal.toEntity(): GroupEntity {
    return GroupEntity(
        id = id,
        name = name,
        description = description,
        startTime = startTime?.toDate(),
        status = status?.toGroupStatus(),
        groupType = groupType?.toGroupType(),
        price = price,
        image = image?.toEntity(),
        meetingsCount = meetingsCount,
        passedCount = passedCount,
        createdAt = createdAt?.toDate(),
        updatedAt = updatedAt?.toDate(),
        banner = banner?.toEntity(),
        owner = owner?.toEntity(),
        subscribed = subscribed,
        invited = invited,
        blocked = blocked,
        participantsCount = participantsCount,
        invitesCount = invitesCount,
        daysOfWeek = daysOfWeek.map { CalendarDayEntity.values()[it] },
        note = note?.toLocale(),
        meetingFormat = meetingFormat,
        tag = tag?.toEntity(), attendees = emptyList(),
        isPrivate = isPrivate,
        pastMeetingsCount = pastMeetingsCount,
        dates = dates,
        themes = themes,
        duration = duration,
        community = community
    )
}

fun BannerLocal.toEntity() = Banner(
    title = title,
    bannerType = bannerType
)

fun NoteLocal.toLocale(): NoteEntity {
    return NoteEntity(
        content = content,
        updatedAt = updatedAt.toDate()!!
    )
}

fun Int.toGroupStatus(): GroupStatus {
    return GroupStatus.values()[this]
}

fun Int.toGroupType(): GroupType {
    return GroupType.values()[this]
}

fun OwnerLocal.toEntity(): OwnerEntity {
    return OwnerEntity(
        id = id,
        fullName = fullName,
        image = image?.toEntity(),
        rating = rating,
        followed = followed,
        location = location,
        connected = connected
    )
}

fun TagLocal.toEntity(): TagEntity {
    return TagEntity(
        id = id,
        tag = tag
    )
}

fun ChatSocketEventMessage.toEntity(): MessageSocketEntity {
    return MessageSocketEntity(
        id = id,
        message = message,
        status = status,
        edited = edited,
        type = messageType,
        userId = userId,
        createdAt = createdAt,
        updatedAt = updatedAt,
        event = ChatSocketEvent.fromRemoteString(event.orEmpty())
    )
}

