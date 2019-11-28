package com.doneit.ascend.presentation.login.log_in

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.login.models.PresentationLoginModel
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface LogInContract {
    interface ViewModel: BaseViewModel {
        val loginModel: PresentationLoginModel
        val isSignInEnabled: ObservableField<Boolean>
        val errorRes: LiveData<Int?>

        fun singInClick()
        fun signUpClick()
        fun termsClick()
        fun privacyPolicyClick()
        fun forgotPasswordClick()
    }

    interface Router {
        fun navigateToForgotPassword()
        fun navigateToSignUp()
        fun navigateToTerms()
        fun navigateToPrivacyPolicy()
        fun goToMain()
    }
}