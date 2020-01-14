package com.doneit.ascend.presentation.main.video_chat.in_progress

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.main.video_chat.VideoChatActivity
import com.doneit.ascend.presentation.models.StartVideoModel

interface ChatInProgressContract {
    interface ViewModel: BaseViewModel {
        val credentials: LiveData<StartVideoModel>

        fun onPermissionsRequired(resultCode: VideoChatActivity.ResultStatus)
    }
}