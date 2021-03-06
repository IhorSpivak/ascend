package com.doneit.ascend.presentation.main.ascension_plan.spiritual_action_steps

import com.doneit.ascend.presentation.main.base.BaseViewModel

interface SpiritualActionStepsContract {

    interface ViewModel : BaseViewModel {
        fun onAddClick()
        fun goBack()
    }

    interface Router {
        fun onBack()
    }
}