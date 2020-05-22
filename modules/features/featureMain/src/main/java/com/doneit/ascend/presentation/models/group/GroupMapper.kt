package com.doneit.ascend.presentation.models.group

import com.doneit.ascend.domain.entity.CalendarDayEntity
import com.doneit.ascend.domain.entity.dto.CreateGroupDTO
import com.doneit.ascend.domain.entity.dto.GroupListDTO
import com.doneit.ascend.domain.entity.dto.UpdateGroupDTO
import com.doneit.ascend.domain.entity.getDefaultCalendar
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.create_group.CreateGroupViewModel
import com.doneit.ascend.presentation.models.PresentationCreateGroupModel
import com.doneit.ascend.presentation.utils.extensions.TIME_24_FORMAT_DROP_DAY
import com.doneit.ascend.presentation.utils.getNotNull
import java.util.*

fun PresentationCreateGroupModel.toWebinarEntity(is24TimeFormat: Boolean): CreateGroupDTO {
    return CreateGroupDTO(
        name.observableField.getNotNull(),
        description.observableField.getNotNull(),
        actualStartTime.time,
        groupType?.toString() ?: "",
        price.observableField.get()?.toFloatS(),
        image.observableField.getNotNull(),
        participants.get(),
        scheduleDays.toDays(),
        numberOfMeetings.observableField.getNotNull().toInt(),
        meetingFormat.observableField.get(),
        isPrivate.get(),
        tags,
        timeList.map {
            TIME_24_FORMAT_DROP_DAY.apply { timeZone = TimeZone.getTimeZone("GMT") }.format(it.time)
        },
        themesOfMeeting.map { it.observableField.get()!! },
        duration.observableField.getNotNull().toMinutes()
    )
}

fun PresentationCreateGroupModel.toUpdateWebinarEntity(group: GroupEntity): UpdateGroupDTO {

    val emails = mutableListOf<String>()
    participants.get()?.let {
        emails.addAll(it.toMutableList())
    }
    (group.attendees?.map { it.email ?: "" } ?: emptyList()).forEach {
        emails.remove(it)
    }
    return UpdateGroupDTO(
        name.observableField.getNotNull(),
        description.observableField.getNotNull(),
        actualStartTime.time,
        groupType?.toString() ?: "",
        price.observableField.get()?.toFloatS(),
        image.observableField.getNotNull(),
        emails,
        participantsToDelete.get(),
        scheduleDays.toDays(),
        numberOfMeetings.observableField.getNotNull().toInt(),
        meetingFormat.observableField.get(),
        isPrivate.get(),
        tags,
        timeList.map {
            TIME_24_FORMAT_DROP_DAY.apply { timeZone = TimeZone.getTimeZone("GMT") }.format(it.time)
        },
        themesOfMeeting.map { it.observableField.get()!! },
        duration.observableField.getNotNull().toMinutes()
    )
}
fun PresentationCreateGroupModel.toEntity(): CreateGroupDTO {
    val startTime =
        CreateGroupViewModel.START_TIME_FORMATTER.parse(startDate.observableField.getNotNull())
    val calendar = getDefaultCalendar()
    calendar.time = startTime!!
    calendar.set(Calendar.HOUR, hours.toHours())
    calendar.set(Calendar.MINUTE, minutes.toInt())
    calendar.set(Calendar.AM_PM, timeType.toAM_PM())

    return CreateGroupDTO(
        name.observableField.getNotNull(),
        description.observableField.getNotNull(),
        calendar.time,
        groupType?.toString() ?: "",
        price.observableField.get()?.toFloatS(),
        image.observableField.getNotNull(),
        participants.get(),
        scheduleDays.toDays(),
        Integer.parseInt(numberOfMeetings.observableField.getNotNull()),
        meetingFormat.observableField.get(),
        isPrivate.get(),
        tags,
        null,
        null,
        duration.observableField.getNotNull().toMinutes()
    )
}

fun PresentationCreateGroupModel.toUpdateEntity(invitedMembers: List<String>): UpdateGroupDTO {
    val calendar = getDefaultCalendar()
    calendar.time =
        CreateGroupViewModel.START_TIME_FORMATTER.parse(startDate.observableField.getNotNull())!!
    calendar.set(Calendar.HOUR, hours.toHours())
    calendar.set(Calendar.MINUTE, minutes.toInt())
    calendar.set(Calendar.AM_PM, timeType.toAM_PM())
    val emails = mutableListOf<String>()
    participants.get()?.let {
        emails.addAll(it.toMutableList())
    }
    invitedMembers.forEach {
        emails.remove(it)
    }
    return UpdateGroupDTO(
        name.observableField.getNotNull(),
        description.observableField.getNotNull(),
        calendar.time,
        groupType?.toString() ?: "",
        price.observableField.get()?.toFloatS(),
        image.observableField.getNotNull(),
        emails,
        participantsToDelete.get(),
        scheduleDays.toDays(),
        Integer.parseInt(numberOfMeetings.observableField.getNotNull()),
        meetingFormat.observableField.get(),
        isPrivate.get(),
        tags,
        null,
        null,
        duration.observableField.getNotNull().toMinutes()
    )
}

fun GroupEntity.toUpdatePrivacyGroupDTO(isPrivate: Boolean): UpdateGroupDTO {
    return UpdateGroupDTO(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        isPrivate,
        null,
        null,
        null,
        null
    )
}

fun PresentationGroupListModel.toDTO(default: GroupListDTO): GroupListDTO {
    return GroupListDTO(
        page = default.page,
        perPage = default.perPage,
        sortColumn = default.sortColumn,
        sortType = default.sortType,
        name = default.name,
        userId = default.userId,
        groupType = default.groupType,
        groupStatus = default.groupStatus,
        myGroups = default.myGroups,
        startDateFrom = dateFrom,
        startDateTo = dateTo,
        daysOfWeen = daysOfWeek,
        numberOfParticipants = numberOfParticipants
    )
}

private fun String.toFloatS(): Float? {
    var res: Float? = null

    try {
        res = this.toFloat()
    } catch (e: NumberFormatException) {
        e.printStackTrace()
    }

    return res
}

private fun List<CalendarDayEntity>.toDays(): List<Int> {
    return this.map {
        it.ordinal
    }
}

private fun String.toHours(): Int {
    return this.toInt() % 12 //% 12to avoid day increment
}

private fun String.toMinutes(): Int {
    return this.toInt() * 60
}

private fun String.toAM_PM(): Int {
    return if (this == "AM") Calendar.AM else Calendar.PM
}
