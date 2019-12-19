package com.doneit.ascend.presentation.splash

import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.vrgsoft.core.presentation.fragment.BaseViewModelImpl

class SplashViewModel(
    private val userUseCase: UserUseCase
) : BaseViewModelImpl(), SplashContract.ViewModel {
    override val user = userUseCase.getUserLive()
}