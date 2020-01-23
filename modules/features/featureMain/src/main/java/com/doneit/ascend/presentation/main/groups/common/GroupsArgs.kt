package com.doneit.ascend.presentation.main.groups.common

import com.doneit.ascend.domain.entity.dto.GroupType
import com.vrgsoft.core.presentation.fragment.argumented.BaseArguments
import kotlinx.android.parcel.Parcelize

@Parcelize
class GroupsArgs(
    val userId: Long? = null,
    val groupType: GroupType? = null,
    val isMyGroups: Boolean? = null
) : BaseArguments()