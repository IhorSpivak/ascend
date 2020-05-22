package com.doneit.ascend.presentation.video_chat_webinar.in_progress.owner_options

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface OwnerOptionsContract {
    interface ViewModel : BaseViewModel {
        val groupInfo: LiveData<GroupEntity>
        val isVideoEnabled: LiveData<Boolean>
        val isAudioRecording: LiveData<Boolean>
        val isMuted: LiveData<Boolean>

        fun switchVideoEnabledState()
        fun switchAudioEnabledState()
        fun onChatClick()
        fun onQuestionsClick()
        fun switchCamera()
        fun finishCall()
        fun onBackClick()
    }
}