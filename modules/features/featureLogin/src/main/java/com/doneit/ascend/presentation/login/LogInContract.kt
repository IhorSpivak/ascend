package com.doneit.ascend.presentation.login

import com.vrgsoft.core.presentation.fragment.BaseViewModel

interface LogInContract {
    interface ViewModel: BaseViewModel {

    }

    interface Router {
        fun navigateToSignUp()
    }
}