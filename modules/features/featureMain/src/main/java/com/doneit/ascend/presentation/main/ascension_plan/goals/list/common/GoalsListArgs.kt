package com.doneit.ascend.presentation.main.ascension_plan.goals.list.common

import com.vrgsoft.core.presentation.fragment.argumented.BaseArguments
import kotlinx.android.parcel.Parcelize

@Parcelize
class GoalsListArgs (
    val isCompleted: Boolean?
) : BaseArguments()