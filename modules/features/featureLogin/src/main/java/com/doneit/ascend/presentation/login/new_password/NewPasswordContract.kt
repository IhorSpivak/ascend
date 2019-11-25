package com.doneit.ascend.presentation.login.new_password

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.login.models.PresentationNewPasswordModel
import com.doneit.ascend.presentation.login.new_password.common.NewPasswordArgs
import com.vrgsoft.core.presentation.fragment.argumented.ArgumentedViewModel
import com.vrgsoft.networkmanager.livedata.SingleLiveManager

interface NewPasswordContract {
    interface ViewModel : ArgumentedViewModel<NewPasswordArgs> {
        val newPasswordModel: PresentationNewPasswordModel
        val canSave: LiveData<Boolean>
        val showSuccessSendSMSMessage: SingleLiveManager<Boolean>

        fun removeErrors()
        fun saveClick()
        fun resendCodeClick()
        fun onBackClick()
    }

    interface Router {
        fun navigateToLogInFragment()
    }
}