package com.doneit.ascend.presentation.video_chat

import android.os.Bundle
import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.PresentationChatParticipant

interface VideoChatContract {
    interface ViewModel : BaseViewModel {
        val groupInfo: LiveData<GroupEntity>
        val timerLabel: LiveData<String>
        val participants: LiveData<List<PresentationChatParticipant>>
        val isFinishing: LiveData<Boolean>
        val finishingLabel: LiveData<String>
        val navigation: LiveData<Navigation>

        fun init(groupId: Long)
        fun finishCall()
        fun onBackClick()
        fun report(content: String, participantId: String)
        fun onNetworkStateChanged(hasConnection: Boolean)
        fun onParticipantClick(id: String)
    }

    interface Router {
        fun onBack()
        fun finishActivity()
        fun navigateToPreview()
        fun navigateToChatInProgress()
        fun navigateToChatFinishScreen()
        fun navigateUserChatOptions()
        fun navigateToMMChatOptions()
        fun navigateToChatParticipantActions(userId: String)
        fun navigateToPermissionsRequiredDialog(resultCode: VideoChatActivity.ResultStatus)
        fun navigateToAttachments(groupId: Long)
        fun navigateToNotes(groupId: Long)
        fun navigateToGoal(groupId: Long)
    }

    enum class Navigation {
        BACK,
        FINISH_ACTIVITY,
        TO_PREVIEW,
        TO_CHAT_IN_PROGRESS,
        TO_CHAT_FINISH,
        TO_USER_CHAT_OPTIONS,
        TO_MM_CHAT_OPTIONS,
        TO_CHAT_PARTICIPANT_ACTIONS,
        TO_PERMISSIONS_REQUIRED_DIALOG,
        TO_ATTACHMENTS,
        TO_NOTES,
        TO_GOAL;

        val data: Bundle = Bundle()
    }
}