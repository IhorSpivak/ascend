package com.doneit.ascend.presentation.main.create_group

import com.vrgsoft.core.presentation.fragment.argumented.BaseArguments
import kotlinx.android.parcel.Parcelize

@Parcelize
class CreateGroupArgs(
    val groupType: String
) : BaseArguments()