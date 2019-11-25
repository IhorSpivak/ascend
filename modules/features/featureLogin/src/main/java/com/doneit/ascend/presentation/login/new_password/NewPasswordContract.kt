package com.doneit.ascend.presentation.login.new_password

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.login.models.PresentationNewPasswordModel
import com.doneit.ascend.presentation.login.new_password.common.NewPasswordArgs
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedViewModel
import com.vrgsoft.networkmanager.livedata.SingleLiveManager

interface NewPasswordContract {
    interface ViewModel : ArgumentedViewModel<NewPasswordArgs> {
        val newPasswordModel: PresentationNewPasswordModel
        val canSave: LiveData<Boolean>

        fun removeErrors()
        fun saveClick()
        fun resendCodeClick()
        fun onBackClick()
    }

    interface Router {
        fun goBackToLogin()
        fun goBack()
    }
}