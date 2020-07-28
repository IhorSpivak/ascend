package com.doneit.ascend.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.dto.AnswersDTO
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.interactor.answer.AnswerUseCase
import com.doneit.ascend.domain.use_case.interactor.chats.ChatUseCase
import com.doneit.ascend.domain.use_case.interactor.notification.NotificationUseCase
import com.doneit.ascend.domain.use_case.interactor.question.QuestionUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.PresentationCommunityModel
import com.doneit.ascend.presentation.models.toPresentationCommunity
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import kotlinx.coroutines.launch

class MainViewModel(
    private val router: MainContract.Router,
    private val userUseCase: UserUseCase,
    private val notificationUseCase: NotificationUseCase,
    private val chatUseCase: ChatUseCase,
    private val questionUseCase: QuestionUseCase,
    private val answerUseCase: AnswerUseCase

) : BaseViewModelImpl(), MainContract.ViewModel {

    override val hasUnread =
        notificationUseCase.getUnreadLive().map { it.find { it.isRead.not() } != null }

    override val hasUnreadMessages = MutableLiveData<Boolean>(false)
    override val isMasterMind = MutableLiveData<Boolean>(false)
    override val communities = MutableLiveData<List<PresentationCommunityModel>>()

    private val user = MutableLiveData<UserEntity?>()
    private val localUser = userUseCase.getUserLive()
    private val userObserver: Observer<UserEntity?> = Observer {
        it?.let {
            user.postValue(it)
            isMasterMind.postValue(it.isMasterMind)
        }
    }

    init {
        localUser.observeForever(userObserver)

        viewModelScope.launch {
            val result = questionUseCase.getList()
            val userLocal = userUseCase.getUser()

            if (result.isSuccessful) {
                communities.postValue(result.successModel!!.community!!.answerOptions.map {
                    it.toPresentationCommunity(it == userLocal?.community)
                })
            } else {
                showDefaultErrorMessage(result.errorModel!!.toErrorMessage())
            }
        }
    }

    override fun saveCommunity(community: String) {
        community.let { newCommunity ->
            if (newCommunity != localUser.value?.community) {
                viewModelScope.launch {
                    val result = answerUseCase.createAnswers(AnswersDTO(newCommunity, listOf()))
                    if (result.isSuccessful) {
                        router.navigateToHome()
                    } else {
                        showDefaultErrorMessage(result.errorModel!!.toErrorMessage())
                    }
                }
            }
        }
    }

    override fun getUnreadMessageCount() {
        viewModelScope.launch {
            hasUnreadMessages.value = chatUseCase.getUnreadMessageCount() > 0
        }
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

    override fun onFilterClick() {
        //TODO:
    }

    override fun onChatClick() {
        router.navigateToMyChats()
    }

    override fun onCleared() {
        user.removeObserver(userObserver)
        super.onCleared()
    }
}