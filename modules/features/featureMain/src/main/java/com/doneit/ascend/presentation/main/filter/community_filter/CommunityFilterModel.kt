package com.doneit.ascend.presentation.main.filter.community_filter

import com.doneit.ascend.domain.entity.dto.GroupListDTO
import com.doneit.ascend.domain.entity.user.Community
import com.doneit.ascend.presentation.main.filter.DayOfWeek
import com.doneit.ascend.presentation.main.filter.FilterModel
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
open class CommunityFilterModel(
    override val selectedDays: MutableList<DayOfWeek> = mutableListOf(),
    override var timeFrom: Long = 0,
    override var timeTo: Long = 0,
    open var community: Community? = Community.values()[0]
) : FilterModel(selectedDays, timeFrom, timeTo) {
    override fun toDTO(dto: GroupListDTO?): GroupListDTO {
        return dto?.copy(
            daysOfWeen = selectedDays.map { it.ordinal },
            timeFrom = timeFrom,
            timeTo = timeTo,
            community = community?.title
        ) ?: GroupListDTO(
            daysOfWeen = selectedDays.map { it.ordinal },
            timeFrom = timeFrom,
            timeTo = timeTo,
            community = community?.title
        )
    }

    companion object {
        fun create(dto: GroupListDTO?): CommunityFilterModel {
            dto ?: return CommunityFilterModel()
            return CommunityFilterModel(
                selectedDays = dto.daysOfWeen?.map { DayOfWeek.values()[it] }.orEmpty()
                    .toMutableList(),
                timeFrom = dto.timeFrom ?: 0,
                timeTo = dto.timeTo ?: 0,
                community = dto.community?.let { Community.valueOf(it.toUpperCase(Locale.getDefault())) }
            )
        }
    }
}