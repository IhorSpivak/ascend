package com.doneit.ascend.presentation.profile.edit_phone.verify_phone

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.EditPhoneModel

interface VerifyChangePhoneContract {
    interface ViewModel: BaseViewModel {
        val dataModel: EditPhoneModel
        val canVerify: LiveData<Boolean>
        val canResendCode: LiveData<Boolean>
        val timerValue: LiveData<String>

        fun sendCode(shouldBlock: Boolean = false)
        fun onVerifyClick()
        fun onBackClick()
    }
}