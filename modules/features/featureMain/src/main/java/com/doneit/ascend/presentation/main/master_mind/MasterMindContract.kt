package com.doneit.ascend.presentation.main.master_mind

import com.doneit.ascend.presentation.main.base.BaseViewModel

interface MasterMindContract {
    interface ViewModel : BaseViewModel {
        fun onSearchClick()
        fun goBack()
    }

    interface Router {
        fun onBack()
        fun navigateToSearch()
    }
}