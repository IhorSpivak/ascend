package com.doneit.ascend.presentation.main.master_mind.list.common

import com.vrgsoft.core.presentation.fragment.argumented.BaseArguments
import kotlinx.android.parcel.Parcelize

@Parcelize
class ListArgs(
    val isFollowed: Boolean?
) : BaseArguments()