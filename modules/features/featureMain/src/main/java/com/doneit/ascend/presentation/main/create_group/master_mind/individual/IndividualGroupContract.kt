package com.doneit.ascend.presentation.main.create_group.master_mind.individual

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.AttachmentEntity
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.PresentationCreateGroupModel
import com.doneit.ascend.presentation.models.ValidatableField
import com.vrgsoft.networkmanager.livedata.SingleLiveManager

interface IndividualGroupContract {
    interface ViewModel : BaseViewModel {
        val createGroupModel: PresentationCreateGroupModel
        var email: ValidatableField
        val canComplete: LiveData<Boolean>
        val canAddParticipant: LiveData<Boolean>
        val participants: LiveData<List<String>>
        val networkErrorMessage: SingleLiveManager<String>
        val clearReservationSeat: SingleLiveManager<Boolean>
        val changeGroup: LiveData<GroupEntity>
        val members: LiveData<MutableList<AttendeeEntity>>
        val selectedMembers: MutableList<AttendeeEntity>

        fun addNewParticipant()
        fun completeClick()
        fun backClick()
        fun chooseScheduleTouch()
        fun chooseStartDateTouch()
        fun addMember(isPublic: Boolean)
    }
}