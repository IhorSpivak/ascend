package com.doneit.ascend.presentation.video_chat_webinar.finished

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface WebinarFinishedContract {
    interface ViewModel: BaseViewModel {
        val groupInfo: LiveData<GroupEntity>

        fun onOkClick()
    }
}