package com.doneit.ascend.presentation.video_chat.in_progress.user_options

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface UserChatOptionsContract {
    interface ViewModel : BaseViewModel {
        val groupInfo: LiveData<GroupEntity>
        val isVideoEnabled: LiveData<Boolean>
        val isAudioEnabled: LiveData<Boolean>
        val isHandRisen: LiveData<Boolean>

        fun switchVideoEnabledState()
        fun switchAudioEnabledState()
        fun switchCamera()
        fun switchHand()
        fun reportGroupOwner(content: String)
        fun finishCall()
        fun onBackClick()

        fun attachments(groupId: Long)
    }
}