package com.doneit.ascend.presentation.main.filter.community_filter

import com.doneit.ascend.domain.entity.user.Community
import com.doneit.ascend.presentation.main.filter.DayOfWeek
import com.doneit.ascend.presentation.main.filter.FilterModel
import kotlinx.android.parcel.Parcelize

@Parcelize
open class CommunityFilterModel(
    override val selectedDays: MutableList<DayOfWeek> = mutableListOf(),
    override var timeFrom: Long = 0,
    override var timeTo: Long = 0,
    var community: Community? = Community.values()[0]
) : FilterModel(selectedDays, timeFrom, timeTo)