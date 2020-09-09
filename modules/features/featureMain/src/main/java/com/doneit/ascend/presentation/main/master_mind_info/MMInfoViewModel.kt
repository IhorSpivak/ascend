package com.doneit.ascend.presentation.main.master_mind_info

import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.doneit.ascend.domain.entity.dto.CreateChatDTO
import com.doneit.ascend.domain.entity.user.Community
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.interactor.chats.ChatUseCase
import com.doneit.ascend.domain.use_case.interactor.master_mind.MasterMindUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.chats.chat.common.ChatType
import com.doneit.ascend.presentation.main.home.community_feed.share_post.SharePostBottomSheetFragment
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import com.vrgsoft.networkmanager.livedata.SingleLiveManager
import kotlinx.coroutines.launch
import java.util.*

@CreateFactory
@ViewModelDiModule
class MMInfoViewModel(
    private val router: MMInfoContract.Router,
    private val userUseCase: UserUseCase,
    private val chatUseCase: ChatUseCase,
    private val masterMindUseCase: MasterMindUseCase
) : BaseViewModelImpl(), MMInfoContract.ViewModel {

    private val idProfile = MutableLiveData<Long>()
    override val profile = idProfile.switchMap { masterMindUseCase.getProfile(it) }
    override val user = MutableLiveData<UserEntity?>()

    override val isFollowVisible = MutableLiveData(false)
    override val isUnfollowVisible = MutableLiveData(false)
    override val enableFollow = MutableLiveData(true)
    override val enableUnfollow = MutableLiveData(true)

    override val showRatingBar = MediatorLiveData<Boolean>()
    override val rated = profile.map { it?.rated ?: false }
    override val myRating = profile.map { it?.myRating }

    override val sendReportStatus = SingleLiveManager<Boolean>()

    @ExperimentalStdlibApi
    override val masterMindDescription =
        profile.map {
            it?.communities?.joinToString(postfix = " ") {
                it.capitalize(Locale.getDefault())
            }.orEmpty()
        }

    private val localUser = userUseCase.getUserLive()
    private val userObserver: Observer<UserEntity?> = Observer {
        it?.let {
            user.postValue(it)
        }
    }

    init {
        localUser.observeForever(userObserver)
        showRatingBar.addSource(user) {
            updateUIVisibility(it, profile.value)
        }

        showRatingBar.addSource(profile) {
            updateUIVisibility(user.value, it)
        }
    }

    override fun startChatWithMM(id: Long) {
        viewModelScope.launch {
            chatUseCase.createChat(CreateChatDTO("", listOf(id.toInt() ))).let {
                if (it.isSuccessful) {
                    user.value?.let { user ->
                        router.navigateToChat(it.successModel!!, user, ChatType.CHAT)
                    }
                } else {
                    showDefaultErrorMessage(it.errorModel!!.toErrorMessage())
                }
            }
        }
    }

    override fun getListOfTitles(): List<Int> {
        profile.value?.community?.let {
            return when (it.capitalize()) {
                Community.SUCCESS.title,
                Community.INDUSTRY.title -> listOf(
                    R.string.webinars,
                    R.string.collaboration,
                    R.string.master_mind,
                    R.string.channels_title,
                    R.string.posts,
                    R.string.bio
                )
                Community.LIFESTYLE.title -> listOf(
                    R.string.webinars,
                    R.string.collaboration,
                    R.string.coaching,
                    R.string.channels_title,
                    R.string.posts,
                    R.string.bio

                )
                Community.RECOVERY.title -> listOf(
                    R.string.webinars,
                    R.string.groups,
                    R.string.master_mind,
                    R.string.channels_title,
                    R.string.posts,
                    R.string.bio
                )
                Community.FAMILY.title -> listOf(
                    R.string.webinars,
                    R.string.groups,
                    R.string.coaching,
                    R.string.channels_title,
                    R.string.posts,
                    R.string.bio
                )
                Community.SPIRITUAL.title -> listOf(
                    R.string.webinars,
                    R.string.collaboration,
                    R.string.coaching,
                    R.string.channels_title,
                    R.string.posts,
                    R.string.bio
                )
                else -> throw IllegalArgumentException("Unknown community: $it")
            }
        }
        return emptyList()
    }

    private fun updateUIVisibility(currentUser: UserEntity?, displayedUser: UserEntity?) {
        if (currentUser != null && displayedUser != null) {
            if (currentUser.id == displayedUser.id) {
                isFollowVisible.value = false
                isUnfollowVisible.value = false
                showRatingBar.value = false
            } else {
                isFollowVisible.value = displayedUser.followed.not() && displayedUser.isMasterMind
                isUnfollowVisible.value = displayedUser.followed && displayedUser.isMasterMind
                showRatingBar.value = displayedUser.allowRating && displayedUser.isMasterMind
            }
        }
    }

    override fun setProfileId(id: Long) {
        idProfile.postValue(id)
    }

    override fun report(content: String) {
        profile.value?.let {
            viewModelScope.launch {
                val res = userUseCase.report(content, it.id.toString())
                if (res.isSuccessful.not()) {
                    showDefaultErrorMessage(res.errorModel!!.toErrorMessage())
                }
            }
        }
    }

    override fun block() {
        profile.value?.let {
            viewModelScope.launch {
                chatUseCase.blockUser(it.id).let {
                    if (it.isSuccessful.not()) {
                        showDefaultErrorMessage(it.errorModel!!.toErrorMessage())
                    }
                }
            }
        }
    }

    override fun onShareInAppClick() {
        user.value?.let {
            router.navigateToShare(
                profile.value!!.id,
                it,
                SharePostBottomSheetFragment.ShareType.PROFILE
            )
        }
    }

    override fun goBack() {
        router.onBack()
    }

    override fun onFollowClick() {
        profile.value?.let {
            viewModelScope.launch {
                enableFollow.postValue(false)
                masterMindUseCase.follow(it.id)
                isFollowVisible.postValue(false)
                isUnfollowVisible.postValue(true)
                enableFollow.postValue(true)
            }
        }
    }

    override fun onUnfollowClick() {
        profile.value?.let {
            viewModelScope.launch {
                enableUnfollow.postValue(false)
                masterMindUseCase.unfollow(it.id)
                isFollowVisible.postValue(true)
                isUnfollowVisible.postValue(false)
                enableUnfollow.postValue(true)
            }
        }
    }

    override fun setRating(rating: Int) {
        profile.value?.let {
            viewModelScope.launch {
                masterMindUseCase.setRating(it.id, rating)
            }
        }
    }

    override fun onSeeGroupsClick() {
        profile.value?.let {
            router.navigateToGroupList(it.id, null, null, it.fullName)
        }
    }

    override fun sendReport(content: String) {
        profile.value?.let {
            viewModelScope.launch {
                val response = masterMindUseCase.sendReport(it.id, content)

                if (response.isSuccessful) {
                    sendReportStatus.call(true)
                } else {
                    sendReportStatus.call(false)
                }
            }
        }
    }

    override fun onCleared() {
        localUser.removeObserver(userObserver)
        super.onCleared()

    }
}