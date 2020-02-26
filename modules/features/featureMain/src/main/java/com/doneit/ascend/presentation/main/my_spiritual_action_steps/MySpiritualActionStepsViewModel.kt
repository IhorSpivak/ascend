package com.doneit.ascend.presentation.main.my_spiritual_action_steps

import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class MySpiritualActionStepsViewModel(
    private val spiritualActionUseCase: SpiritualActionUseCase,
    private val router: MySpiritualActionStepsContract.Router
) : BaseViewModelImpl(), MySpiritualActionStepsContract.ViewModel {

    override fun goBack() {
        router.onBack()
    }

    override fun onAddClick() {

    }
}