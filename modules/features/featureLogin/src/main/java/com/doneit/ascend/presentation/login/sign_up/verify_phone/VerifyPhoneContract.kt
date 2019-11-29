package com.doneit.ascend.presentation.login.sign_up.verify_phone

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.login.models.PresentationSignUpModel
import com.doneit.ascend.presentation.main.base.BaseViewModel
import java.util.*

interface VerifyPhoneContract {
    interface ViewModel : BaseViewModel {
        val registrationModel: PresentationSignUpModel
        val canContinue: LiveData<Boolean>
        val canResendCode: LiveData<Boolean>
        val canShowTimer: LiveData<Boolean>
        val timerValue: LiveData<String>
        val sendTimer: Timer?
        val end: Long

        fun onVerifyClick()

        fun sendCode(isStartTimer: Boolean = false)
        fun onBackClick(clearModel: Boolean)
    }
}