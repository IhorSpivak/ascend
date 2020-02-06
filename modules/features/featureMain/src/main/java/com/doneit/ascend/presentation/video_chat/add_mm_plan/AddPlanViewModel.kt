package com.doneit.ascend.presentation.video_chat.add_mm_plan

import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class AddPlanViewModel(
    private val router: AddPlanContract.Router
) : BaseViewModelImpl(), AddPlanContract.ViewModel {
    override fun backClick() {
        router.onBack()
    }

    override fun addSpiritualPlanClick() {
        //TODO:
    }

    override fun addGoalClick() {
        //TODO:
    }
}