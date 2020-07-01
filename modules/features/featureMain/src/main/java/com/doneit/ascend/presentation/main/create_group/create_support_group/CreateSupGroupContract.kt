package com.doneit.ascend.presentation.main.create_group.create_support_group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.TagEntity
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedViewModel
import com.doneit.ascend.presentation.main.create_group.CreateGroupArgs
import com.doneit.ascend.presentation.main.create_group.common.IClickListener
import com.doneit.ascend.presentation.models.PresentationCreateGroupModel
import com.doneit.ascend.presentation.models.ValidatableField
import com.doneit.ascend.presentation.utils.GroupAction
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
        val tags: MutableLiveData<List<TagEntity>>
        val supportTitle: LiveData<Int>

        fun addNewParticipant()
        fun removeMember(member: AttendeeEntity): Int
        fun completeClick()
        fun updateGroup(group: GroupEntity)
        fun backClick()
        fun changeSchedule()
        fun chooseScheduleTouch()
        fun chooseStartDateTouch()
        fun chooseMeetingCountTouch(group: GroupEntity?, what: GroupAction?)
        fun addMember(groupType: GroupType)
    }
}