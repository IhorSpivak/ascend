package com.doneit.ascend.presentation.main.goals

import com.doneit.ascend.presentation.main.base.BaseViewModel

interface GoalsContract {
    interface ViewModel : BaseViewModel {
        fun onAddClick()
        fun goBack()
    }

    interface Router {
        fun onBack()
    }
}