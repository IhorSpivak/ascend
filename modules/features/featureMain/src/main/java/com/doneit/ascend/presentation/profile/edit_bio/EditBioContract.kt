package com.doneit.ascend.presentation.profile.edit_bio

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.models.ValidatableField

interface EditBioContract {

    interface ViewModel {
        val bioValue: ValidatableField
        val canSaveBio: LiveData<Boolean>

        fun updateBio(newBio: String)
    }
}