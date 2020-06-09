package com.doneit.ascend.presentation.main.master_mind_info

import androidx.lifecycle.*
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.interactor.master_mind.MasterMindUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
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
    private val masterMindUseCase: MasterMindUseCase
) : BaseViewModelImpl(), MMInfoContract.ViewModel {

    private val groupId = MutableLiveData<Long>()
    override val profile = groupId.switchMap { masterMindUseCase.getProfile(it) }
    override val user: LiveData<UserEntity?> = userUseCase.getUserLive()

    override val isFollowVisible = MutableLiveData(true)
    override val isUnfollowVisible = MutableLiveData(true)
    override val enableFollow = MutableLiveData(true)
    override val enableUnfollow = MutableLiveData(true)

    override val showRatingBar = MediatorLiveData<Boolean>()
    override val rated = profile.map { it?.rated ?: false }
    override val myRating = profile.map { it?.myRating }

    override val sendReportStatus = SingleLiveManager<Boolean>()
    @ExperimentalStdlibApi
    override val masterMindDescription =
        profile.map { it?.communities?.joinToString(postfix = " ") {
            it.capitalize(Locale.getDefault())
        }.orEmpty() }

    init {
        showRatingBar.addSource(user) {
            updateUIVisibility(it, profile.value)
        }

        showRatingBar.addSource(profile) {
            updateUIVisibility(user.value, it)
        }
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
        groupId.postValue(id)
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

    override fun goBack() {
        router.onBack()
    }

    override fun onFollowClick() {
        profile.value?.let {
            viewModelScope.launch {
                enableFollow.postValue(false)
                masterMindUseCase.follow(it.id)
                enableFollow.postValue(true)
            }
        }
    }

    override fun onUnfollowClick() {
        profile.value?.let {
            viewModelScope.launch {
                enableUnfollow.postValue(false)
                masterMindUseCase.unfollow(it.id)
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
}