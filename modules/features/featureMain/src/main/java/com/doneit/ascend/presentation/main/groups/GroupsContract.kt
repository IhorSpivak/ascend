package com.doneit.ascend.presentation.main.groups

import com.doneit.ascend.presentation.main.base.BaseViewModel

interface GroupsContract {
    interface ViewModel : BaseViewModel {
        fun backClick()
    }

    interface Router {
        fun onBack()
    }
}