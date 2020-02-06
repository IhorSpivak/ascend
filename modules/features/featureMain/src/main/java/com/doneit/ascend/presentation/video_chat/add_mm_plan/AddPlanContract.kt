package com.doneit.ascend.presentation.video_chat.add_mm_plan

import com.doneit.ascend.presentation.main.base.BaseViewModel

interface AddPlanContract {
    interface ViewModel : BaseViewModel {
        fun backClick()
        fun addSpiritualPlanClick()
        fun addGoalClick()
    }

    interface Router {
        fun onBack()
    }
}