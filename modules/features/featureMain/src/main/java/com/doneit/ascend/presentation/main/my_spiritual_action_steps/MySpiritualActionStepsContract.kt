package com.doneit.ascend.presentation.main.my_spiritual_action_steps

import com.doneit.ascend.presentation.main.base.BaseViewModel

interface MySpiritualActionStepsContract {

    interface ViewModel : BaseViewModel {
        fun onAddClick()
        fun goBack()
    }

    interface Router {
        fun onBack()
    }
}