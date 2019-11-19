package com.doneit.ascend.presentation.login.sign_up

import com.doneit.ascend.domain.entity.SignUpModel
import com.vrgsoft.core.presentation.fragment.BaseViewModel

interface SignUpContract {
    interface ViewModel: BaseViewModel {
        val registrationModel: SignUpModel

        fun registrationClick()
    }

    interface Router {

    }
}