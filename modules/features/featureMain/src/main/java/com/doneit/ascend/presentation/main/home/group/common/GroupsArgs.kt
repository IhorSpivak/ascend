package com.doneit.ascend.presentation.main.home.group.common

import com.vrgsoft.core.presentation.fragment.argumented.BaseArguments
import kotlinx.android.parcel.Parcelize

@Parcelize
class GroupsArgs(
    val groupType: Int,
    val isMineGroups: Boolean?,
    val isAllGroups: Boolean
) : BaseArguments()