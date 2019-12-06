package com.doneit.ascend.presentation.main.home

import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class HomeViewModel(
    private val userUseCase: UserUseCase
) : BaseViewModelImpl(), HomeContract.ViewModel {

    override val user = userUseCase.getUser()
}