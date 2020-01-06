package com.doneit.ascend.presentation.profile.edit_email

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.EditEmailModel

interface EditEmailContract {
    interface ViewModel : BaseViewModel {
        val dataModel: EditEmailModel
        val canSave: LiveData<Boolean>

        fun onSaveClick()
        fun onBackClick()
    }

    interface Router {
        fun onBack()
    }
}