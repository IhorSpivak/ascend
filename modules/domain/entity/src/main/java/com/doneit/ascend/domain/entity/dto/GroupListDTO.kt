package com.doneit.ascend.domain.entity.dto

import com.doneit.ascend.domain.entity.group.GroupStatus
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.entity.group.GroupTypeParticipants
import java.util.*

data class GroupListDTO(
    override val page: Int? = null,
    override val perPage: Int? = null,
    override val sortColumn: String? = null,
    override val sortType: SortType? = null,
    val name: String? = null,
    val userId: Long? = null,
    val groupType: GroupType? = null,
    val groupStatus: GroupStatus? = null,
    val myGroups: Boolean? = null,
    val startDateFrom: Date? = null,
    val startDateTo: Date? = null,
    val timeFrom: Long? = null,
    val timeTo: Long? = null,
    val daysOfWeen: List<Int>? = null,
    val numberOfParticipants: GroupTypeParticipants? = null,
    val community: String? = null,
    val tagId: Int? = null
) : BasePagedDTO(page, perPage, sortColumn, sortType)