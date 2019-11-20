package com.doneit.ascend.presentation.login.sign_up.verify_phone

import com.doneit.ascend.presentation.login.models.PresentationSignUpModel
import com.vrgsoft.core.presentation.fragment.BaseViewModel

interface VerifyPhoneContract {
    interface ViewModel: BaseViewModel {
        val registrationModel: PresentationSignUpModel

        fun onVerifyClick()
        fun sendCode()
        fun onBackClick()
    }
}