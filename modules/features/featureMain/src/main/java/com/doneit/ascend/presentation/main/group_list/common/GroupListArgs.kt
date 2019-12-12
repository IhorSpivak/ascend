package com.doneit.ascend.presentation.main.group_list.common

import com.vrgsoft.core.presentation.fragment.argumented.BaseArguments
import kotlinx.android.parcel.Parcelize

@Parcelize
class GroupListArgs(
    val groupType: Int,
    val isMyGroups: Boolean? = null
) : BaseArguments()