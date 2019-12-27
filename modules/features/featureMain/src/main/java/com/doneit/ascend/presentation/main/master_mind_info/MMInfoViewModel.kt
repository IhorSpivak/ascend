package com.doneit.ascend.presentation.main.master_mind_info

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.use_case.interactor.master_mind.MasterMindUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.vrgsoft.networkmanager.livedata.SingleLiveManager
import kotlinx.coroutines.launch

class MMInfoViewModel(
    private val router: MMInfoContract.Router,
    private val userUseCase: UserUseCase,
    private val masterMindUseCase: MasterMindUseCase
) : BaseViewModelImpl(), MMInfoContract.ViewModel {

    override val profile = MutableLiveData<MasterMindEntity>()
    override val showRatingBar = MutableLiveData<Boolean>(true)
    override val showActionButtons = SingleLiveManager<Boolean>()
    override val enableFollow = MutableLiveData<Boolean>()
    override val enableUnfollow = MutableLiveData<Boolean>()
    override val followed = MutableLiveData<Boolean>()
    override val rated = MutableLiveData<Boolean>(true)
    override val myRating = MutableLiveData<Int?>()

    override val sendReportStatus = SingleLiveManager<Boolean>()

    override fun setModel(model: MasterMindEntity) {
        profile.postValue(model)

        viewModelScope.launch {

            val currUser = userUseCase.getUser()

            if (currUser?.id == model.id) {
                // hide buttons and rating bar
                showRatingBar.postValue(false)
                showActionButtons.call(false)
            } else {
                followed.postValue(model.followed)

                if (model.allowRating == false) {
                    rated.postValue(false)
                } else {
                    rated.postValue(!model.rated)
                }
            }

            myRating.postValue(model.myRating)
        }
    }

    override fun report(content: String) {
        profile.value?.let {
            viewModelScope.launch {
                val res = userUseCase.report(content, it.id)
                if (res.isSuccessful.not()) {
                    showDefaultErrorMessage(res.errorModel!!.toErrorMessage())
                }
            }
        }
    }

    override fun goBack() {
        router.closeActivity()
    }

    override fun onFollowClick() {
        enableFollow.postValue(false)

        profile.value?.let {
            viewModelScope.launch {
                val response = masterMindUseCase.follow(it.id)

                if (response.isSuccessful) {
                    followed.postValue(true)
                }

                enableFollow.postValue(true)
            }
        }
    }

    override fun onUnfollowClick() {
        enableUnfollow.postValue(false)

        profile.value?.let {
            viewModelScope.launch {
                val response = masterMindUseCase.unfollow(it.id)

                if (response.isSuccessful) {
                    followed.postValue(false)
                }

                enableUnfollow.postValue(true)
            }
        }
    }

    override fun setRating(rating: Int) {
        profile.value?.let {
            viewModelScope.launch {
                val response = masterMindUseCase.setRating(it.id, rating)

                if (response.isSuccessful) {
                    myRating.postValue(rating)
                    rated.postValue(false)
                }
            }
        }
    }

    override fun onSeeGroupsClick() {
        profile.value?.let {
            router.navigateToGroupList(it.id)
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