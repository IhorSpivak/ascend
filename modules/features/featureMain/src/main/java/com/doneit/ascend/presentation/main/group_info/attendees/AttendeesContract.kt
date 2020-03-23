package com.doneit.ascend.presentation.main.group_info.attendees

import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface AttendeesContract {
    interface ViewModel: BaseViewModel {
        val groupId: MutableLiveData<Long>
        val attendees: MutableLiveData<MutableList<AttendeeEntity>>
        val selectedMembers: MutableList<AttendeeEntity>

        fun onAdd(member: AttendeeEntity)
        fun onRemove(member: AttendeeEntity)
        fun onInviteClick(email: String)
        fun submitRequest(query: String)
        fun goBack()
        fun onClearClick(member: AttendeeEntity)
        fun onAttendeeClick()
    }

    interface Router{
        fun onBack()
    }
}