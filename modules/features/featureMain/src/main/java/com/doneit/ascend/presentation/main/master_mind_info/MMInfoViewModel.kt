package com.doneit.ascend.presentation.main.master_mind_info

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.MasterMindEntity
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
    override val showActionButtons = SingleLiveManager<Boolean>()
    override val enableFollow = MutableLiveData<Boolean>(true)
    override val enableUnfollow = MutableLiveData<Boolean>(true)
    override val followed = MutableLiveData<Boolean>()
    override val showRatingBar = MutableLiveData<Boolean>(true)
    override val rated = MutableLiveData<Boolean>(true)
    override val myRating = MutableLiveData<Int?>()

    override val sendReportStatus = SingleLiveManager<Boolean>()

    override fun setModel(model: MasterMindEntity) {
        groupId.postValue(model.id)

        viewModelScope.launch {

            val currUser = userUseCase.getUser()

            if (currUser?.id == model.id) {
                // hide buttons and rate bar
                showRatingBar.postValue(false)
                showActionButtons.call(false)
            } else {
                followed.postValue(model.followed)
                showRatingBar.postValue(model.allowRating)
                rated.postValue(model.rated)
            }

            myRating.postValue(model.myRating)
        }
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

                val response = masterMindUseCase.follow(it.id)

                if (response.isSuccessful) {
                    followed.postValue(true)
                }

                enableFollow.postValue(true)
            }
        }
    }

    override fun onUnfollowClick() {
        profile.value?.let {
            viewModelScope.launch {
                enableUnfollow.postValue(false)

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
                    rated.postValue(true)
                }
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