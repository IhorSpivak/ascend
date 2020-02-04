package com.doneit.ascend.presentation.main.create_group.create_support_group

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedViewModel
import com.doneit.ascend.presentation.main.create_group.common.IClickListener
import com.doneit.ascend.presentation.main.create_group.master_mind.CreateGroupArgs
import com.doneit.ascend.presentation.models.PresentationCreateGroupModel
import com.doneit.ascend.presentation.models.ValidatableField
import com.vrgsoft.networkmanager.livedata.SingleLiveManager

interface CreateSupGroupContract {
    interface ViewModel : ArgumentedViewModel<CreateGroupArgs>, IClickListener {
        val createGroupModel: PresentationCreateGroupModel
        var email: ValidatableField
        val canComplete: LiveData<Boolean>
        val canAddParticipant: LiveData<Boolean>
        val participants: LiveData<List<String>>
        val clearReservationSeat: SingleLiveManager<Boolean>

        fun addNewParticipant()
        fun completeClick()
        fun backClick()
        fun chooseScheduleTouch()
        fun chooseStartDateTouch()
    }

    interface Router {
        fun onBack()
        fun navigateToCalendarPiker()
        fun navigateToDatePicker()
    }
}