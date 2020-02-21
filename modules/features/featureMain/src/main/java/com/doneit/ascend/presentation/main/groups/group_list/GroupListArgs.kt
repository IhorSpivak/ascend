package com.doneit.ascend.presentation.main.groups.group_list

import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.entity.group.GroupStatus
import com.doneit.ascend.domain.entity.group.GroupType
import com.vrgsoft.core.presentation.fragment.argumented.BaseArguments
import kotlinx.android.parcel.Parcelize

@Parcelize
class GroupListArgs(
    val sortType: SortType? = null,
    val userId: Long? = null,
    val groupType: GroupType? = null,
    val isMyGroups: Boolean? = null,
    val status: GroupStatus? = null
) : BaseArguments()