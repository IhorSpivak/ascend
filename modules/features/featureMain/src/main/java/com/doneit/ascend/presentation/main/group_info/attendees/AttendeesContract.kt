package com.doneit.ascend.presentation.main.group_info.attendees

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.ParticipantEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.main.create_group.add_member.common.AddMemberViewModel
import com.doneit.ascend.presentation.models.ValidatableField

interface AttendeesContract {
    interface ViewModel: AddMemberViewModel {
    }

    interface Router{
        fun onBack()
    }
}