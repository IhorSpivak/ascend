package com.doneit.ascend.presentation.main.master_mind_profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.use_case.interactor.master_mind.MasterMindUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.vrgsoft.networkmanager.livedata.SingleLiveManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MMProfileViewModel(
    private val router: MMProfileContract.Router,
    private val userUseCase: UserUseCase,
    private val masterMindUseCase: MasterMindUseCase
) : BaseViewModelImpl(), MMProfileContract.ViewModel {

    override val profile = MutableLiveData<MasterMindEntity>()
    override val enableFollow = MutableLiveData<Boolean>(true)
    override val enableUnfollow = MutableLiveData<Boolean>(true)
    override val followed = MutableLiveData<Boolean>()
    override val rated = MutableLiveData<Boolean>(true)
    override val myRating = MutableLiveData<Int?>()

    override val sendReportStatus = SingleLiveManager<Boolean>()

    private var userId: Long = 0

    override fun loadData(id: Long) {
        userId = id

        viewModelScope.launch {
            val response = masterMindUseCase.getProfile(id)

            if (response.isSuccessful) {
                response.successModel?.let {
                    followed.postValue(it.followed)

                    if (it.allowRating == false) {
                        rated.postValue(false)
                    } else {
                        rated.postValue(!it.rated)
                    }

                    myRating.postValue(it.myRating)
                    profile.postValue(it)
                }
            }
        }
    }

    override fun report(content: String) {
        viewModelScope.launch {
            profile.value?.let {
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

        GlobalScope.launch {
            val response = masterMindUseCase.follow(userId)

            if (response.isSuccessful) {
                followed.postValue(true)
            }

            enableFollow.postValue(true)
        }
    }

    override fun onUnfollowClick() {
        enableUnfollow.postValue(false)

        GlobalScope.launch {
            val response = masterMindUseCase.unfollow(userId)

            if (response.isSuccessful) {
                followed.postValue(false)
            }

            enableUnfollow.postValue(true)
        }
    }

    override fun setRating(rating: Int) {
        GlobalScope.launch {
            val response = masterMindUseCase.setRating(userId, rating)

            if (response.isSuccessful) {
                myRating.postValue(rating)
                rated.postValue(false)
            }
        }
    }

    override fun onSeeGroupsClick() {
        router.navigateToGroupList(userId)
    }

    override fun sendReport(content: String) {
        GlobalScope.launch {
            val response = masterMindUseCase.sendReport(userId, content)

            if (response.isSuccessful) {
                sendReportStatus.call(true)
            } else {
                sendReportStatus.call(false)
            }
        }
    }
}