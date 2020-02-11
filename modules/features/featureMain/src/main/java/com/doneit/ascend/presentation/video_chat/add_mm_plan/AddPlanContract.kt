package com.doneit.ascend.presentation.video_chat.add_mm_plan

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface AddPlanContract {
    interface ViewModel : BaseViewModel {
        val navigation: LiveData<Navigation>

        fun backClick()
        fun addSpiritualPlanClick()
        fun addGoalClick()
    }

    interface Router {
        fun onBack()
    }

    enum class Navigation {
        BACK
    }
}