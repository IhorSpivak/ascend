package com.doneit.ascend.presentation.login

import com.doneit.ascend.presentation.login.models.PresentationLoginModel
import com.vrgsoft.core.presentation.fragment.BaseViewModel

interface LogInContract {
    interface ViewModel: BaseViewModel {
        val loginModel: PresentationLoginModel
        fun singInClick()
    }

    interface Router {
        fun navigateToSignUp()
    }
}