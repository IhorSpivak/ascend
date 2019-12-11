package com.doneit.ascend.presentation.main.models

import com.doneit.ascend.domain.entity.dto.CreateGroupModel
import com.doneit.ascend.presentation.utils.CalendarDay
import com.doneit.ascend.presentation.utils.getNotNull

fun PresentationCreateGroupModel.toEntity(groupType: String): CreateGroupModel {
    return CreateGroupModel(
        name.observableField.getNotNull(),
        description.observableField.getNotNull(),
        startDate.observableField.getNotNull(),
        groupType,
        price.observableField.getNotNull(),
        image.observableField.getNotNull(),
        participants.get(),
        scheduleDays.toDays(),
        Integer.parseInt(numberOfMeetings.observableField.getNotNull())
    )
}

fun List<CalendarDay>.toDays(): List<Int> {
    return this.map {
        it.ordinal - 1
    }
}