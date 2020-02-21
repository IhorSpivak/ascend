package com.doneit.ascend.presentation.main.home.daily.common.groups

import com.doneit.ascend.domain.entity.group.GroupType
import com.vrgsoft.core.presentation.fragment.argumented.BaseArguments
import kotlinx.android.parcel.Parcelize

@Parcelize
class GroupsArgs(
    val groupType: GroupType?,
    val isMineGroups: Boolean?
) : BaseArguments()