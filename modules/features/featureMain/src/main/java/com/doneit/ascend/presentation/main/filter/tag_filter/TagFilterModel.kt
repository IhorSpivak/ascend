package com.doneit.ascend.presentation.main.filter.tag_filter

import com.doneit.ascend.domain.entity.dto.GroupListDTO
import com.doneit.ascend.presentation.main.filter.DayOfWeek
import com.doneit.ascend.presentation.main.filter.FilterModel
import kotlinx.android.parcel.Parcelize

@Parcelize
open class TagFilterModel(
    override val selectedDays: MutableList<DayOfWeek> = mutableListOf(),
    override var timeFrom: Long = 0,
    override var timeTo: Long = 0,
    var selectedTagId: Int? = null
) : FilterModel() {
    override fun toDTO(dto: GroupListDTO?): GroupListDTO {
        return dto?.copy(
            daysOfWeen = selectedDays.map { it.ordinal },
            timeFrom = timeFrom,
            timeTo = timeTo,
            tagId = selectedTagId
        )?:GroupListDTO(
            daysOfWeen = selectedDays.map { it.ordinal },
            timeFrom = timeFrom,
            timeTo = timeTo,
            tagId = selectedTagId
        )
    }

    companion object {
        fun create(dto: GroupListDTO?): TagFilterModel {
            dto ?: return TagFilterModel()
            return TagFilterModel(
                selectedDays = dto.daysOfWeen?.map { DayOfWeek.values()[it] }.orEmpty()
                    .toMutableList(),
                timeFrom = dto.timeFrom ?: 0,
                timeTo = dto.timeTo ?: 0,
                selectedTagId = dto.tagId
            )
        }
    }
}