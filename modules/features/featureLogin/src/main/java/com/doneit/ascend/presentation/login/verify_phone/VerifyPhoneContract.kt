package com.doneit.ascend.presentation.login.verify_phone

import com.doneit.ascend.presentation.login.models.PresentationSignUpModel
import com.vrgsoft.core.presentation.fragment.BaseViewModel

interface VerifyPhoneContract {
    interface ViewModel: BaseViewModel {
        fun setModel(signUpModel: PresentationSignUpModel)
        fun onVerifyClick()
    }

    interface Router {
        fun goToMain()
        fun goBack()
    }
}