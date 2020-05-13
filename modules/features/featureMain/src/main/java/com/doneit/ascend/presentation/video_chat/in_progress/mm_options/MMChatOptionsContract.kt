package com.doneit.ascend.presentation.video_chat.in_progress.mm_options

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel


interface MMChatOptionsContract {
    interface ViewModel: BaseViewModel {
        val groupInfo: LiveData<GroupEntity>
        val isVideoEnabled: LiveData<Boolean>
        val isAudioRecording: LiveData<Boolean>
        val isMuted: LiveData<Boolean>
        val isAllMuted: LiveData<Boolean>

        fun switchAllMuted()
        fun onAttachmentsClick()
        fun switchVideoEnabledState()
        fun switchAudioEnabledState()
        fun switchCamera()
        fun finishCall()
        fun onBackClick()
    }
}