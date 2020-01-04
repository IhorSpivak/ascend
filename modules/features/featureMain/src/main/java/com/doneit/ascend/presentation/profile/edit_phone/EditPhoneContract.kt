package com.doneit.ascend.presentation.profile.edit_phone

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.EditPhoneModel

interface EditPhoneContract {
    interface ViewModel : BaseViewModel {
        val dataModel: EditPhoneModel
        val canContinue: LiveData<Boolean>

        fun init()
        fun continueClick()
        fun onBackClick()
    }

    interface Router {
        fun onBack()
        fun navigateToVerifyPhone()
    }
}