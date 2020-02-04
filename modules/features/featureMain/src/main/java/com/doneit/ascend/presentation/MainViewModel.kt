package com.doneit.ascend.presentation

import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import kotlinx.coroutines.launch

class MainViewModel(
    private val router: MainContract.Router,
    private val userUseCase: UserUseCase
) : BaseViewModelImpl(), MainContract.ViewModel {

    private var user: UserEntity? = null

    init {
        viewModelScope.launch {
            user = userUseCase.getUser()
        }
    }

    override fun onCreateGroupClick() {
        user?.let {
            if (it.isMasterMind) {
                router.navigateToCreateGroupMM()
            } else {
                router.navigateToCreateGroupRegular()
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
}