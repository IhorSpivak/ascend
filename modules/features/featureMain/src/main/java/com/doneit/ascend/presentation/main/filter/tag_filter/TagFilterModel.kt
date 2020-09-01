package com.doneit.ascend.presentation.main.filter.tag_filter

import com.doneit.ascend.domain.entity.TagEntity
import com.doneit.ascend.presentation.main.filter.DayOfWeek
import com.doneit.ascend.presentation.main.filter.FilterModel
import kotlinx.android.parcel.Parcelize

@Parcelize
open class TagFilterModel(
    override val selectedDays: MutableList<DayOfWeek> = mutableListOf(),
    override var timeFrom: Long = 0,
    override var timeTo: Long = 0,
    var selectedTag: TagEntity? = null
) : FilterModel()