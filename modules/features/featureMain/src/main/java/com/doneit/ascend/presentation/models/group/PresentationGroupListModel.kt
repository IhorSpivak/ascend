package com.doneit.ascend.presentation.models.group

import com.doneit.ascend.domain.entity.group.GroupTypeParticipants
import java.util.*

data class PresentationGroupListModel(
    var daysOfWeek: List<Int> = listOf(),
    var dateFrom: Date? = null,
    var dateTo: Date? = null,
    var numberOfParticipants: GroupTypeParticipants = GroupTypeParticipants.GROUP
)