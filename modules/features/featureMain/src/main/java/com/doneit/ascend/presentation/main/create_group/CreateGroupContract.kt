package com.doneit.ascend.presentation.main.create_group

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedViewModel
import com.doneit.ascend.presentation.main.create_group.common.IClickListener
import com.doneit.ascend.presentation.main.models.PresentationCreateGroupModel
import com.doneit.ascend.presentation.main.models.ValidatableField

interface CreateGroupContract {
    interface ViewModel : ArgumentedViewModel<CreateGroupArgs>, IClickListener {
        val createGroupModel: PresentationCreateGroupModel
        var email: ValidatableField
        val canCreate: LiveData<Boolean>
        val canAddParticipant: LiveData<Boolean>
        val participants: LiveData<List<String>>

        fun addNewParticipant()
        fun completeClick()
        fun backClick()
        fun chooseScheduleTouch()
    }

    interface Router {
        fun navigateToCalendarPiker()
    }
}