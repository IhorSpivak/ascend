package com.doneit.ascend.presentation.profile.change_password

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.PresentationChangePasswordModel

interface ChangePasswordContract {
    interface ViewModel: BaseViewModel {
        val dataModel: PresentationChangePasswordModel
        val canSave: LiveData<Boolean>

        fun onSaveClick()
        fun onBackClick()
    }

    interface Router {
        fun onBack()
    }
}