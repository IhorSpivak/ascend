package com.doneit.ascend.presentation.login.new_password

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.login.models.PresentationNewPasswordModel
import com.doneit.ascend.presentation.login.new_password.common.NewPasswordArgs
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedViewModel
import java.util.*

interface NewPasswordContract {
    interface ViewModel : ArgumentedViewModel<NewPasswordArgs> {
        val newPasswordModel: PresentationNewPasswordModel
        val canSave: LiveData<Boolean>
        val canResendCode: LiveData<Boolean>
        val canShowTimer: LiveData<Boolean>
        val timerValue: LiveData<String>
        val sendTimer: Timer?
        val end: Long

        fun removeErrors()
        fun saveClick()
        fun resendCodeClick()
        fun onBackClick()
    }

    interface Router {
        fun onBack()
        fun goToMain()
    }
}