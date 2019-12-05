package com.doneit.ascend.presentation.main.create_group

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.main.models.PresentationCreateGroupModel
import com.doneit.ascend.presentation.main.models.ValidatableField

interface CreateGroupContract {
    interface ViewModel : BaseViewModel {
        val createGroupModel: PresentationCreateGroupModel
        var email: ValidatableField
        val canCreate: LiveData<Boolean>
        val canAddParticipant: LiveData<Boolean>
        val participants: LiveData<List<String>>

        fun addNewParticipant()
    }

    interface Router
}