package com.doneit.ascend.presentation.main.create_group

import com.vrgsoft.core.presentation.fragment.argumented.BaseArguments
import kotlinx.android.parcel.Parcelize

@Parcelize
class AddMemberArgs(
    val individual: Boolean
) : BaseArguments()