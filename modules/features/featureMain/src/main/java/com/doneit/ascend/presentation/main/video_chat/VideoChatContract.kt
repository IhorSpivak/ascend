package com.doneit.ascend.presentation.main.video_chat

import com.doneit.ascend.presentation.main.base.BaseViewModel

interface VideoChatContract {
    interface ViewModel : BaseViewModel {

        fun init(groupId: Long)
        fun onBackClick()
    }

    interface Router {
        fun onBack()
        fun navigateToChatInProgress()
    }
}