package com.doneit.ascend.presentation.main.filter

import android.os.Parcelable
import com.doneit.ascend.domain.entity.dto.GroupListDTO
import kotlinx.android.parcel.Parcelize

@Parcelize
open class FilterModel(
    open val selectedDays: MutableList<DayOfWeek> = mutableListOf(),
    open var timeFrom: Long = 0,
    open var timeTo: Long = 0
) : Parcelable {
    open fun toDTO(dto: GroupListDTO? = null): GroupListDTO {
        return dto?.copy(
            daysOfWeen = selectedDays.map { it.ordinal },
            timeFrom = timeFrom,
            timeTo = timeTo
        ) ?: GroupListDTO(
            daysOfWeen = selectedDays.map { it.ordinal },
            timeFrom = timeFrom,
            timeTo = timeTo
        )
    }
}