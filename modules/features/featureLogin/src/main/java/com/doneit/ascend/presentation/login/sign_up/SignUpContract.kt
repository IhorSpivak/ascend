package com.doneit.ascend.presentation.login.sign_up

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.login.models.PresentationSignUpModel
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface SignUpContract {
    interface ViewModel: BaseViewModel {
        val registrationModel: PresentationSignUpModel
        val canContinue: LiveData<Boolean>

        fun removeErrors()
        fun continueClick()
        fun onBackClick()
    }

    interface Router {
        fun navigateToVerifyPhone()
        fun goToMain()
        fun navigateToFirstTimeLogin()
        fun goBack()
    }
}