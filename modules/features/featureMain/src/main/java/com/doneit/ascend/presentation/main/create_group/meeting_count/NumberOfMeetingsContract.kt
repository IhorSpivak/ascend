package com.doneit.ascend.presentation.main.create_group.meeting_count

import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.PresentationCreateGroupModel

interface NumberOfMeetingsContract {
    interface ViewModel : BaseViewModel {
        val meetingsCountOk: MutableLiveData<Boolean>
        val createGroupModel: PresentationCreateGroupModel
        val themesOfMeeting: MutableLiveData<Int>

        fun backDateClick()
        fun backClick()
        fun okMeetingCountClick()
        fun setMeetingCount(count: String)
        fun updateNumberOfMeeting(count: Int)
    }
}