package com.doneit.ascend.presentation.main.create_group.create_support_group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedViewModel
import com.doneit.ascend.presentation.main.create_group.CreateGroupArgs
import com.doneit.ascend.presentation.main.create_group.common.IClickListener
import com.doneit.ascend.presentation.models.GroupType
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
        val members: MutableLiveData<MutableList<AttendeeEntity>>
        val selectedMembers: MutableList<AttendeeEntity>

        fun addNewParticipant()
        fun removeMember(member: AttendeeEntity)
        fun completeClick()
        fun updateGroup(id: Long)
        fun backClick()
        fun changeSchedule()
        fun chooseScheduleTouch()
        fun chooseStartDateTouch()
        fun chooseMeetingCountTouch()
        fun addMember(groupType: GroupType)
    }
}