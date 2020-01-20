package com.doneit.ascend.presentation.video_chat

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.SocketEventEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface VideoChatContract {
    interface ViewModel : BaseViewModel {
        val groupInfo: LiveData<GroupEntity>
        val timerLabel: LiveData<String>
        val messages: LiveData<SocketEventEntity>
        val participants: LiveData<List<SocketEventEntity>>
        val isFinishing: LiveData<Boolean>
        val finishingLabel: LiveData<String>

        fun init(groupId: Long)
        fun finishCall()
        fun onBackClick()
    }

    interface Router {
        fun onBack()
        fun finishActivity()
        fun navigateToPreview()
        fun navigateToChatInProgress()
        fun navigateToChatFinishScreen()
        fun navigateUserChatOptions()
        fun navigateToMMChatOptions()
        fun navigateToPermissionsRequiredDialog(resultCode: VideoChatActivity.ResultStatus)
    }
}