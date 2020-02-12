package com.doneit.ascend.presentation.video_chat.in_progress.user_options

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface UserChatOptionsContract {
    interface ViewModel : BaseViewModel {
        val groupInfo: LiveData<GroupEntity>
        val isVideoEnabled: LiveData<Boolean>
        val isAudioRecording: LiveData<Boolean>
        val isMuted: LiveData<Boolean>
        val isHandRisen: LiveData<Boolean>

        fun onNotesClick()
        fun onAttachmentsClick()
        fun onGoalClick()
        fun switchVideoEnabledState()
        fun switchAudioEnabledState()
        fun switchCamera()
        fun switchHand()
        fun reportGroupOwner(content: String)
        fun finishCall()
        fun onBackClick()
    }
}