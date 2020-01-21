package com.doneit.ascend.presentation.video_chat.in_progress.user_actions

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.SocketEventEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface ChatParticipantActionsContract {
    interface ViewModel: BaseViewModel {
        val participants: LiveData<List<SocketEventEntity>>

        fun report(content: String, participantId: Long)
        fun onBackClick()
    }
}