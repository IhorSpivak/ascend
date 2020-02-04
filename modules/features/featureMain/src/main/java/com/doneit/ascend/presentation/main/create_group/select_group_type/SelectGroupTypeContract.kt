package com.doneit.ascend.presentation.main.create_group.select_group_type

import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.GroupType

interface SelectGroupTypeContract {
    interface ViewModel : BaseViewModel {
        fun selectGroupType(type: GroupType)
        fun backClick()
    }

    interface Router {
        fun onBack()
        fun navigateToCreateGroup(type: GroupType)
    }
}