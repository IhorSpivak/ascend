package com.doneit.ascend.presentation.login.first_time_login.common

import com.doneit.ascend.domain.entity.QuestionEntity
import com.vrgsoft.core.presentation.fragment.argumented.BaseArguments
import kotlinx.android.parcel.Parcelize

@Parcelize
class FirstTimeLoginArgs(
    val questions: List<QuestionEntity>
) : BaseArguments()