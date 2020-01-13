package com.doneit.ascend.presentation.main.video_chat

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.StartVideoModel

interface VideoChatContract {
    interface ViewModel : BaseViewModel {
        val credentials: LiveData<StartVideoModel>

        fun init(groupId: Long)
        fun onBackClick()
    }

    interface Router {
        fun onBack()
    }
}