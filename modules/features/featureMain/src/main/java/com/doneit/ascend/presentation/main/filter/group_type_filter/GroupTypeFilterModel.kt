package com.doneit.ascend.presentation.main.filter.group_type_filter

import com.doneit.ascend.domain.entity.dto.GroupListDTO
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.entity.user.Community
import com.doneit.ascend.presentation.main.filter.DayOfWeek
import com.doneit.ascend.presentation.main.filter.community_filter.CommunityFilterModel
import kotlinx.android.parcel.Parcelize

@Parcelize
open class GroupTypeFilterModel(
    override val selectedDays: MutableList<DayOfWeek> = mutableListOf(),
    override var timeFrom: Long = 0,
    override var timeTo: Long = 0,
    override var community: List<Community>? = listOf(Community.values()[0]),
    open var groupType: GroupType? = null
) : CommunityFilterModel() {
    override fun toDTO(dto: GroupListDTO?): GroupListDTO {
        return dto?.copy(
            groupType = groupType,
            daysOfWeen = selectedDays.map { it.ordinal },
            timeFrom = timeFrom,
            timeTo = timeTo,
            community = community?.joinToString(separator = ",") { it.title.toLowerCase() }
        )?:GroupListDTO(
            groupType = groupType,
            daysOfWeen = selectedDays.map { it.ordinal },
            timeFrom = timeFrom,
            timeTo = timeTo,
            community = community?.joinToString(separator = ",") { it.title.toLowerCase() }
        )
    }

    companion object {
        fun create(dto: GroupListDTO?): GroupTypeFilterModel {
            dto ?: return GroupTypeFilterModel()
            return GroupTypeFilterModel(
                selectedDays = dto.daysOfWeen?.map { DayOfWeek.values()[it] }.orEmpty()
                    .toMutableList(),
                timeFrom = dto.timeFrom ?: 0,
                timeTo = dto.timeTo ?: 0,
                community = dto.community?.split(',')?.map { Community.valueOf(it.toUpperCase()) },
                groupType = dto.groupType
            )
        }
    }
}