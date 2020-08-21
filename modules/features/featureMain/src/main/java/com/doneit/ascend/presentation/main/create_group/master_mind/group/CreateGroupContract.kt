package com.doneit.ascend.presentation.main.create_group.master_mind.group

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedViewModel
import com.doneit.ascend.presentation.main.create_group.CreateGroupArgs
import com.doneit.ascend.presentation.main.create_group.common.IClickListener
import com.doneit.ascend.presentation.models.PresentationCreateGroupModel
import com.doneit.ascend.presentation.models.ValidatableField
import com.doneit.ascend.presentation.utils.GroupAction
import com.google.android.material.textfield.TextInputEditText
import com.vrgsoft.networkmanager.livedata.SingleLiveManager

interface CreateGroupContract {
    interface ViewModel : ArgumentedViewModel<CreateGroupArgs>, IClickListener {
        val createGroupModel: PresentationCreateGroupModel
        var email: ValidatableField
        val canComplete: LiveData<Boolean>
        val canAddParticipant: LiveData<Boolean>
        val canAddMembers: LiveData<Boolean>
        val participants: LiveData<List<String>>
        val networkErrorMessage: SingleLiveManager<String>
        val clearReservationSeat: SingleLiveManager<Boolean>
        val changeGroup: LiveData<GroupEntity>
        val members: LiveData<MutableList<AttendeeEntity>>
        val selectedMembers: MutableList<AttendeeEntity>
        val nonMembers: MutableList<String>
        val supportTitle: LiveData<Int>

        fun addNewParticipant()
        fun removeMember(member: AttendeeEntity): Int
        fun completeClick()
        fun backClick()
        fun chooseScheduleTouch()
        fun chooseMeetingCountTouch(group: GroupEntity?, what: GroupAction?)
        fun chooseStartDateTouch()
        fun addMember(groupType: GroupType)
        fun inviteToGroup(participants: List<String>)
        fun onPriceClick(editor: TextInputEditText)
        fun okPriceClick(price: String)
    }
}