package com.doneit.ascend.presentation.main.master_mind_info

import androidx.lifecycle.*
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.interactor.master_mind.MasterMindUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import com.vrgsoft.networkmanager.livedata.SingleLiveManager
import kotlinx.coroutines.launch

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

    override val isFollowVisible = MutableLiveData<Boolean>(true)
    override val isUnfollowVisible = MutableLiveData<Boolean>(true)
    override val enableFollow = MutableLiveData<Boolean>(true)
    override val enableUnfollow = MutableLiveData<Boolean>(true)

    override val showRatingBar = MediatorLiveData<Boolean>()
    override val rated = profile.map { it?.rated ?: false }
    override val myRating = profile.map { it?.myRating }

    override val sendReportStatus = SingleLiveManager<Boolean>()

    init {
        showRatingBar.addSource(user) {
            updateUIVisibility(it, profile.value)
        }

        showRatingBar.addSource(profile) {
            updateUIVisibility(user.value, it)
        }
    }

    private fun updateUIVisibility(currentUser: UserEntity?, masterMind: MasterMindEntity?) {
        if (currentUser != null && masterMind != null) {
            if (currentUser.id == masterMind.id) {
                isFollowVisible.value = false
                isUnfollowVisible.value = false
                showRatingBar.value = false
            } else {
                isFollowVisible.value = masterMind.followed.not()
                isUnfollowVisible.value = masterMind.followed
                showRatingBar.value = masterMind.allowRating ?: false
            }
        }
    }

    override fun setMMId(id: Long) {
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
            router.navigateToGroupList(it.id, null, null)
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