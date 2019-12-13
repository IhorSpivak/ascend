package com.doneit.ascend.presentation.main.search

import com.doneit.ascend.presentation.main.base.BaseViewModel

interface SearchContract {
    interface ViewModel: BaseViewModel {
        fun goBack()
    }

    interface Router {
        fun closeActivity()
    }
}