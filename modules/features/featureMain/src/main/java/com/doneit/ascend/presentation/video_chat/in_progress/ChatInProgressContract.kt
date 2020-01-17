package com.doneit.ascend.presentation.video_chat.in_progress

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.StartVideoModel
import com.doneit.ascend.presentation.video_chat.VideoChatActivity

interface ChatInProgressContract {
    interface ViewModel : BaseViewModel {
        val credentials: LiveData<StartVideoModel>
        val groupInfo: LiveData<GroupEntity>
        val isVideoEnabled: LiveData<Boolean>
        val isAudioEnabled: LiveData<Boolean>

        fun onPermissionsRequired(resultCode: VideoChatActivity.ResultStatus)
        fun forceDisconnect()
        fun onOpenOptions()
    }

    interface ViewModelLocal : BaseViewModel {
        val isVideoEnabled: LiveData<Boolean>

        fun changeVideoEnabledState()
    }
}