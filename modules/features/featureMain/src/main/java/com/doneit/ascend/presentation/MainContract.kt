package com.doneit.ascend.presentation

import com.doneit.ascend.presentation.main.base.BaseViewModel

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
        fun navigateToCreateGroupRegular()
        fun navigateToHome()
        fun navigateToMyContent()
        fun navigateToAscensionPlan()
        fun navigateToMMProfile()
        fun navigateToRegularUserProfile()
    }
}