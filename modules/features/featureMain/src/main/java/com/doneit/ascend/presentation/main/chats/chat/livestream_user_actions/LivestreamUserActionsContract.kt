package com.doneit.ascend.presentation.main.chats.chat.livestream_user_actions

import com.doneit.ascend.presentation.main.base.BaseViewModel

interface LivestreamUserActionsContract {
    interface ViewModel : BaseViewModel {

        fun remove(userId: Long)
        fun report(content: String, participantId: String)
        fun onBackClick()
    }
}