package com.doneit.ascend.presentation.login

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.login.models.PresentationLoginModel
import com.vrgsoft.core.presentation.fragment.BaseViewModel

interface LogInContract {
    interface ViewModel: BaseViewModel {
        val loginModel: PresentationLoginModel
        val isSignInEnabled: ObservableField<Boolean>
        val errorMessage: LiveData<Int>

        fun singInClick()
    }

    interface Router {
        fun navigateToSignUp()
        fun goToMain()
    }
}