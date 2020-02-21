package com.doneit.ascend.presentation.login.log_in

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.presentation.login.models.PresentationLoginModel
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.facebook.AccessToken
import com.vrgsoft.networkmanager.livedata.SingleLiveManager

interface LogInContract {
    interface ViewModel: BaseViewModel {
        val loginModel: PresentationLoginModel
        val isSignInEnabled: ObservableField<Boolean>
        val facebookNeedLoginSubject: SingleLiveManager<Boolean>
        val twitterNeedLoginSubject: SingleLiveManager<Boolean>
        val errorRes: LiveData<Int?>
        val user: LiveData<UserEntity?>

        fun singInClick()
        fun signUpClick()
        fun termsClick()
        fun privacyPolicyClick()
        fun forgotPasswordClick()
        fun onFacebookLoginClick()
        fun onFacebookLogin(accessToken: AccessToken)
        fun onGoogleLoginClick()
        fun loginWithGoogle(idToken: String)
        fun onTwitterLoginClick()
        fun loginWithTwitter(token: String, secretToken: String)
    }

    interface Router {
        fun navigateToForgotPassword()
        fun navigateToSignUp()
        fun navigateToTerms()
        fun navigateToPrivacyPolicy()
        fun navigateToFirstTimeLogin()
        fun goToMain()
        fun navigateToGoogleLogin()
    }
}