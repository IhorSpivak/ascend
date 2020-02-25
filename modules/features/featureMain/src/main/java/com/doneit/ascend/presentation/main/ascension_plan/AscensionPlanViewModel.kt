package com.doneit.ascend.presentation.main.ascension_plan

import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class AscensionPlanViewModel(
    private val userUseCase: UserUseCase,
    private val router: AscensionPlanContract.Router
) : BaseViewModelImpl(), AscensionPlanContract.ViewModel {

    override val user = userUseCase.getUserLive()
}