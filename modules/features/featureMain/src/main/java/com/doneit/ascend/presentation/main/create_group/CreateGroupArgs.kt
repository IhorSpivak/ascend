package com.doneit.ascend.presentation.main.create_group

import com.doneit.ascend.domain.entity.group.GroupType
import com.vrgsoft.core.presentation.fragment.argumented.BaseArguments
import kotlinx.android.parcel.Parcelize

@Parcelize
class CreateGroupArgs(
    val groupType: GroupType
) : BaseArguments()