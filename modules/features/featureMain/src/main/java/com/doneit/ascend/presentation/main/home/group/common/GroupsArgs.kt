package com.doneit.ascend.presentation.main.home.group.common

import com.doneit.ascend.domain.entity.dto.GroupType
import com.vrgsoft.core.presentation.fragment.argumented.BaseArguments
import kotlinx.android.parcel.Parcelize

@Parcelize
class GroupsArgs(
    val groupType: GroupType?,
    val isMineGroups: Boolean?
) : BaseArguments()