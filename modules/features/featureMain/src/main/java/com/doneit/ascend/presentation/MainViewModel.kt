package com.doneit.ascend.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.interactor.chats.ChatUseCase
import com.doneit.ascend.domain.use_case.interactor.notification.NotificationUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import kotlinx.coroutines.launch

class MainViewModel(
    private val router: MainContract.Router,
    private val userUseCase: UserUseCase,
    private val notificationUseCase: NotificationUseCase,
    private val chatUseCase: ChatUseCase
) : BaseViewModelImpl(), MainContract.ViewModel {
    override fun onFilterClick() {
        //TODO:
    }

    override fun onChatClick() {
        router.navigateToMyChats()
    }

    override val hasUnread =
        notificationUseCase.getUnreadLive().map { it.find { it.isRead.not() } != null }

    override val hasUnreadMessages = MutableLiveData<Boolean>(false)

    override fun getUnreadMessageCount() {
        viewModelScope.launch {
            hasUnreadMessages.value = chatUseCase.getUnreadMessageCount() > 0
        }
    }

    private val user = MutableLiveData<UserEntity?>()
    private val localUser = userUseCase.getUserLive()
    private val userObserver: Observer<UserEntity?> = Observer {
        it?.let {
            user.postValue(it)
        }
    }

    init {
        localUser.observeForever(userObserver)
    }

    override fun onSearchClick() {
        router.navigateToSearch()
    }

    override fun onNotificationClick() {
        router.navigateToNotifications()
    }

    override fun onCreateGroupClick() {
        user.value?.let {
            val permission = it.communities?.contains(it.community?.toLowerCase()) ?: false
            if (it.isMasterMind && permission) {
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
        user.value?.let {
            if (it.isMasterMind) {
                router.navigateToMMProfile()
            } else {
                router.navigateToRegularUserProfile()
            }
        }
    }

    override fun tryToNavigateToGroupInfo(id: Long) {
        viewModelScope.launch {
            if (userUseCase.hasSignedInUser()) {
                router.navigateToGroupInfo(id)
            } else {
                router.navigateToLogin()
            }
        }
    }

    override fun onCleared() {
        user.removeObserver(userObserver)
        super.onCleared()
    }
}