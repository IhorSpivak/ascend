package com.doneit.ascend.presentation.models.group

import com.doneit.ascend.domain.entity.CalendarDayEntity
import com.doneit.ascend.domain.entity.dto.CreateGroupDTO
import com.doneit.ascend.domain.entity.dto.GroupListDTO
import com.doneit.ascend.domain.entity.getDefaultCalendar
import com.doneit.ascend.presentation.main.create_group.CreateGroupViewModel
import com.doneit.ascend.presentation.models.PresentationCreateGroupModel
import com.doneit.ascend.presentation.utils.getNotNull
import java.util.*

fun PresentationCreateGroupModel.toEntity(): CreateGroupDTO {
    val startTime =
        CreateGroupViewModel.START_TIME_FORMATTER.parse(startDate.observableField.getNotNull())
    val calendar = getDefaultCalendar()
    calendar.time = startTime!!
    calendar.set(Calendar.HOUR, hours.toHours())
    //calendar.set(Calendar.HOUR_OF_DAY, hours.toInt())
    calendar.set(Calendar.MINUTE, minutes.toInt())
    val type = if (hours.toInt() > 12){
        Calendar.PM
    }else{
        timeType.toAM_PM()
    }
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
        tags
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

private fun String.toAM_PM(): Int {
    return if (this == "AM") Calendar.AM else Calendar.PM
}
