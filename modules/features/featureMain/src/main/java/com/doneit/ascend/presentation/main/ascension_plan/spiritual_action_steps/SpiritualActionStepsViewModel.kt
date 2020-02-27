package com.doneit.ascend.presentation.main.ascension_plan.spiritual_action_steps

import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class SpiritualActionStepsViewModel(
    private val router: SpiritualActionStepsContract.Router
) : BaseViewModelImpl(), SpiritualActionStepsContract.ViewModel {

    override fun goBack() {
        router.onBack()
    }

    override fun onAddClick() {

    }
}