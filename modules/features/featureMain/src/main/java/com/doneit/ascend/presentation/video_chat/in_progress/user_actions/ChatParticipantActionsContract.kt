package com.doneit.ascend.presentation.video_chat.in_progress.user_actions

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.PresentationChatParticipant

interface ChatParticipantActionsContract {
    interface ViewModel: BaseViewModel {
        val participants: LiveData<List<PresentationChatParticipant>>

        fun report(content: String, participantId: Long)
        fun removeChatParticipant(id: Long)
        fun onBackClick()
    }
}