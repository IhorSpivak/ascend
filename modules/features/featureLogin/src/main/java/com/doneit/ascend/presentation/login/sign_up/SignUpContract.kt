package com.doneit.ascend.presentation.login.sign_up

import com.doneit.ascend.presentation.login.models.PresentationSignUpModel
import com.vrgsoft.core.presentation.fragment.BaseViewModel

interface SignUpContract {
    interface ViewModel: BaseViewModel {
        val registrationModel: PresentationSignUpModel

        fun continueClick()
        fun onBackClick()
    }

    interface Router {
        fun navigateToVerifyPhone(model: PresentationSignUpModel)
        fun goBack()
    }
}