package com.doneit.ascend.presentation.main.create_group

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedViewModel
import com.doneit.ascend.presentation.main.create_group.common.IClickListener
import com.doneit.ascend.presentation.main.models.PresentationCreateGroupModel
import com.doneit.ascend.presentation.main.models.ValidatableField
import com.vrgsoft.networkmanager.livedata.SingleLiveManager

interface CreateGroupContract {
    interface ViewModel : ArgumentedViewModel<CreateGroupArgs>, IClickListener {
        val createGroupModel: PresentationCreateGroupModel
        var email: ValidatableField
        val canComplete: LiveData<Boolean>
        val canAddParticipant: LiveData<Boolean>
        val participants: LiveData<List<String>>
        val networkErrorMessage: SingleLiveManager<String>

        fun addNewParticipant()
        fun completeClick()
        fun backClick()
        fun chooseScheduleTouch()
        fun chooseStartDateTouch()
    }

    interface Router {
        fun onBack()
        fun closeActivity()
        fun navigateToCalendarPiker()
        fun navigateToDatePicker()
    }
}