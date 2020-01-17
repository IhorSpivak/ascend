package com.doneit.ascend.presentation.video_chat.in_progress.mm_options

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel


interface MMChatOptionsContract {
    interface ViewModel: BaseViewModel {
        val groupInfo: LiveData<GroupEntity>
        val isVideoEnabled: LiveData<Boolean>
        val isAudioEnabled: LiveData<Boolean>

        fun changeVideoEnabledState()
        fun changeAudioEnabledState()
        fun onBackClick()
    }
}