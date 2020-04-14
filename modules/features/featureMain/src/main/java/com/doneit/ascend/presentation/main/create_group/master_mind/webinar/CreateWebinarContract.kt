package com.doneit.ascend.presentation.main.create_group.master_mind.webinar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedViewModel
import com.doneit.ascend.presentation.main.create_group.CreateGroupArgs
import com.doneit.ascend.presentation.main.create_group.common.IClickListener
import com.doneit.ascend.presentation.models.GroupType
import com.doneit.ascend.presentation.models.PresentationCreateGroupModel
import com.doneit.ascend.presentation.models.ValidatableField
import com.doneit.ascend.presentation.utils.GroupAction
import com.google.android.material.textfield.TextInputEditText
import com.vrgsoft.networkmanager.livedata.SingleLiveManager

interface CreateWebinarContract {
    interface ViewModel : ArgumentedViewModel<CreateGroupArgs>, IClickListener {
        val createGroupModel: PresentationCreateGroupModel
        val canComplete: LiveData<Boolean>
        val canAddParticipant: LiveData<Boolean>
        val participants: LiveData<List<String>>
        val networkErrorMessage: SingleLiveManager<String>
        val members: MutableLiveData<MutableList<AttendeeEntity>>
        val selectedMembers: MutableList<AttendeeEntity>
        val themesOfMeeting: MutableLiveData<Int>
        val newScheduleItem: MutableLiveData<MutableList<ValidatableField>>
        val themes: MutableLiveData<MutableList<ValidatableField>>

        fun addNewParticipant()
        fun removeMember(member: AttendeeEntity)
        fun completeClick()
        fun updateGroup(group: GroupEntity)
        fun backClick()
        fun changeSchedule()
        fun chooseScheduleTouch()
        fun chooseScheduleTouch(position: Int)
        fun chooseStartDateTouch()
        fun onSelectStartDate()
        fun addMember(groupType: GroupType)
        fun onPriceClick(editor: TextInputEditText)
        fun chooseMeetingCountTouch(group: GroupEntity?, what: GroupAction?)
        fun updateListOfTimes(position: Int, remove: Boolean)
        fun updateListOfTimes(remove: Boolean)
        fun updateFields(group: GroupEntity, what: String)
    }
}