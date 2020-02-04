package com.doneit.ascend.presentation.video_chat

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.PresentationChatParticipant

interface VideoChatContract {
    interface ViewModel : BaseViewModel {
        val groupInfo: LiveData<GroupEntity>
        val timerLabel: LiveData<String>
        val participants: LiveData<List<PresentationChatParticipant>>
        val isFinishing: LiveData<Boolean>
        val finishingLabel: LiveData<String>

        fun init(groupId: Long)
        fun finishCall()
        fun onBackClick()
        fun report(content: String, participantId: Long)
        fun onNetworkStateChanged(hasConnection: Boolean)
        fun onParticipantClick(id: Long)
    }

    interface Router {
        fun onBack()
        fun finishActivity()
        fun navigateToPreview()
        fun navigateToChatInProgress()
        fun navigateToChatFinishScreen()
        fun navigateUserChatOptions()
        fun navigateToMMChatOptions()
        fun navigateToChatParticipantActions(userId: Long)
        fun navigateToPermissionsRequiredDialog(resultCode: VideoChatActivity.ResultStatus)
        fun navigateToAttachments(groupId: Long)
    }
}