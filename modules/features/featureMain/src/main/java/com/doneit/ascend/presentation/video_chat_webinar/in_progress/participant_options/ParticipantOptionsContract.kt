package com.doneit.ascend.presentation.video_chat_webinar.in_progress.participant_options

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface ParticipantOptionsContract {
    interface ViewModel : BaseViewModel {
        val groupInfo: LiveData<GroupEntity>

        fun onNotesClick()
        fun onChatClick()
        fun reportGroupOwner(content: String)
        fun leaveGroup()
        fun onBackClick()
    }
}