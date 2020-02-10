package com.doneit.ascend.presentation

import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.domain.use_case.interactor.notification.NotificationUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.GroupType
import kotlinx.coroutines.launch

class MainViewModel(
    private val router: MainContract.Router,
    private val userUseCase: UserUseCase,
    private val notificationUseCase: NotificationUseCase
) : BaseViewModelImpl(), MainContract.ViewModel {

    override val hasUnread = notificationUseCase.getUnreadLive().map { it.find { it.isRead.not() } != null }
    private var user: UserEntity? = null

    init {
        viewModelScope.launch {
            user = userUseCase.getUser()
        }
    }

    override fun onSearchClick() {
        router.navigateToSearch()
    }

    override fun onNotificationClick() {
        router.navigateToNotifications()
    }

    override fun onCreateGroupClick() {
        user?.let {
            if (it.isMasterMind) {
                router.navigateToCreateGroupMM()
            } else {
                router.navigateToCreateGroup(GroupType.SUPPORT)
            }
        }
    }

    override fun onHomeClick() {
        router.navigateToHome()
    }

    override fun navigateToMyContent() {
        router.navigateToMyContent()
    }

    override fun navigateToAscensionPlan() {
        router.navigateToAscensionPlan()
    }

    override fun navigateToProfile() {
        user?.let {
            if (it.isMasterMind) {
                router.navigateToMMProfile()
            } else {
                router.navigateToRegularUserProfile()
            }
        }
    }

    override fun navigateToGroupInfo(id: Long) {
        router.navigateToGroupInfo(id)
    }
}