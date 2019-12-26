package com.doneit.ascend.presentation.models

import com.doneit.ascend.domain.entity.dto.CreateGroupModel
import com.doneit.ascend.domain.entity.CalendarDayEntity
import com.doneit.ascend.domain.entity.ProfileEntity
import com.doneit.ascend.domain.entity.dto.UpdateProfileModel
import com.doneit.ascend.presentation.main.create_group.CreateGroupViewModel
import com.doneit.ascend.presentation.utils.getNotNull
import java.util.*

fun PresentationCreateGroupModel.toEntity(groupType: String): CreateGroupModel {
    val startTime = CreateGroupViewModel.START_TIME_FORMATTER.parse(startDate.observableField.getNotNull())
    val calendar = Calendar.getInstance()
    calendar.time = startTime
    calendar.set(Calendar.HOUR, hours.toInt())
    calendar.set(Calendar.MINUTE, minutes.toInt())
    calendar.set(Calendar.AM_PM, timeType.toAM_PM())


    return CreateGroupModel(
        name.observableField.getNotNull(),
        description.observableField.getNotNull(),
        calendar.time,
        groupType,
        price.observableField.getNotNull(),
        image.observableField.getNotNull(),
        participants.get(),
        scheduleDays.toDays(),
        Integer.parseInt(numberOfMeetings.observableField.getNotNull())
    )
}

fun ProfileEntity.toDTO(): UpdateProfileModel {
    return UpdateProfileModel(
        fullName,
        displayName,
        location,
        meetingStarted,
        newGroups,
        inviteToMeeting,
        -1,
        bio,
        description,
        false,
        null
    )
}

fun List<CalendarDayEntity>.toDays(): List<Int> {
    return this.map {
        it.ordinal
    }
}

fun String.toAM_PM(): Int {
    return if(this == "AM") Calendar.AM else Calendar.PM
}