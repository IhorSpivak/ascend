package com.doneit.ascend.domain.entity.dto

import com.doneit.ascend.domain.entity.group.GroupStatus
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.entity.group.GroupTypeParticipants
import java.util.*

class GroupListDTO(
    page: Int? = null,
    perPage: Int? = null,
    sortColumn: String? = null,
    sortType: SortType? = null,
    val name: String? = null,
    val userId: Long? = null,
    val groupType: GroupType? = null,
    val groupStatus: GroupStatus? = null,
    val myGroups: Boolean? = null,
    val startDateFrom: Date? = null,
    val startDateTo: Date? = null,
    val daysOfWeen: List<Int>? = null,
    val numberOfParticipants: GroupTypeParticipants? = null
) : BasePagedDTO(page, perPage, sortColumn, sortType)