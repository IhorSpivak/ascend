package com.doneit.ascend.presentation.video_chat.add_mm_plan

import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import com.vrgsoft.networkmanager.livedata.SingleLiveEvent

@CreateFactory
@ViewModelDiModule
class AddPlanViewModel : BaseViewModelImpl(), AddPlanContract.ViewModel {

    override val navigation = SingleLiveEvent<AddPlanContract.Navigation>()

    override fun backClick() {
        navigation.postValue(AddPlanContract.Navigation.BACK)
    }

    override fun addSpiritualPlanClick() {
        //TODO:
    }

    override fun addGoalClick() {
        //TODO:
    }
}