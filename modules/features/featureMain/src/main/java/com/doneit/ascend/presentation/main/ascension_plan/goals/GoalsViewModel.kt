package com.doneit.ascend.presentation.main.goals

import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class GoalsViewModel(
    private val router: GoalsContract.Router
) : BaseViewModelImpl(), GoalsContract.ViewModel {

    override fun goBack() {
        router.onBack()
    }

    override fun onAddClick() {

    }
}