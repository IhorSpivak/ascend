package com.doneit.ascend.presentation.video_chat.in_progress.user_options

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface UserChatOptionsContract {
    interface ViewModel : BaseViewModel {
        val groupInfo: LiveData<GroupEntity>
        val isVideoEnabled: LiveData<Boolean>

        fun changeVideoEnabledState()
        fun onLeaveGroupClick()
        fun onBackClick()
    }
}