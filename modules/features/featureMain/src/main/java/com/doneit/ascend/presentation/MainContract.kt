package com.doneit.ascend.presentation

import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.GroupType

interface MainContract {
    interface ViewModel: BaseViewModel {
        fun onCreateGroupClick()
        fun onHomeClick()
        fun navigateToMyContent()
        fun navigateToAscensionPlan()
        fun navigateToProfile()
    }

    interface Router {
        fun navigateToCreateGroupMM()
        fun navigateToCreateGroup(type: GroupType)
        fun navigateToHome()
        fun navigateToMyContent()
        fun navigateToAscensionPlan()
        fun navigateToMMProfile()
        fun navigateToRegularUserProfile()
    }
}