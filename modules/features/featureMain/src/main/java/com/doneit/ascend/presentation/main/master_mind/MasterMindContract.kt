package com.doneit.ascend.presentation.main.master_mind

import com.doneit.ascend.presentation.main.base.BaseViewModel

interface MasterMindContract {
    interface ViewModel : BaseViewModel {
        fun goBack()
    }

    interface Router {
        fun closeActivity()
    }
}