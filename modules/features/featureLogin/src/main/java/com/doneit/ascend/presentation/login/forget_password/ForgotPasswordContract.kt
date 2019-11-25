package com.doneit.ascend.presentation.login.forget_password

import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.presentation.login.models.PresentationPhoneModel
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface ForgotPasswordContract {
    interface ViewModel: BaseViewModel {
        val phoneModel : PresentationPhoneModel
        val canContinue : MutableLiveData<Boolean>

        fun onBackClick()
        fun resetClick()
    }

    interface Router {
        fun goBack()
    }
}