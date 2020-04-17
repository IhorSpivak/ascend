package com.doneit.ascend.presentation

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.GroupType

interface MainContract {
    interface ViewModel: BaseViewModel {
        val hasUnread: LiveData<Boolean>
        val hasUnreadMessages: LiveData<Boolean>

        fun onNotificationClick()
        fun onSearchClick()
        fun onFilterClick()
        fun onChatClick()

        fun onCreateGroupClick()
        fun onHomeClick()
        fun navigateToMyContent()
        fun navigateToAscensionPlan()
        fun navigateToProfile()
        fun tryToNavigateToGroupInfo(id: Long)
    }

    interface Router {
        fun navigateToSearch()
        fun navigateToNotifications()
        fun navigateToGroupInfo(id: Long)
        fun navigateToLogin()
        fun navigateToMyChats()

        fun navigateToCreateGroupMM()
        fun navigateToCreateGroup(type: GroupType)
        fun navigateToHome()
        fun navigateToMyContent()
        fun navigateToAscensionPlan()
        fun navigateToMMProfile()
        fun navigateToRegularUserProfile()
    }
}