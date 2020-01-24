package com.doneit.ascend.presentation.profile.edit_bio

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.ValidatableField

interface EditBioContract {

    interface ViewModel: BaseViewModel {
        val bioValue: ValidatableField
        val canSave: LiveData<Boolean>

        fun updateBio(newBio: String)
        fun goBack()
    }

    interface Router {
        fun onBack()
    }
}