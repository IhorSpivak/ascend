package com.doneit.ascend.presentation.main.create_group.master_mind

import com.doneit.ascend.presentation.models.GroupType
import com.vrgsoft.core.presentation.fragment.argumented.BaseArguments
import kotlinx.android.parcel.Parcelize

@Parcelize
class CreateGroupArgs(
    val groupType: GroupType
) : BaseArguments()