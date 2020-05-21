package com.doneit.ascend.presentation.video_chat_webinar

import android.os.Bundle
import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface WebinarVideoChatContract {
    interface ViewModel : BaseViewModel {
        val groupInfo: LiveData<GroupEntity>
        val timerLabel: LiveData<String>
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
        fun canGoBack(): Boolean
        fun onBack()
        fun finishActivity()
        fun navigateToPreview()
        fun navigateToChatInProgress()
        fun navigateToChatFinishScreen()
        fun navigateUserChatOptions()
        fun navigateToMMChatOptions()
        fun navigateToChatParticipantActions(userId: String)
        fun navigateToPermissionsRequiredDialog(resultCode: WebinarVideoChatActivity.ResultStatus)
        fun navigateToQuestions(groupId: Long)
        fun navigateToChat(groupId: Long)
        fun navigateToNotes(groupId: Long)
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
        TO_QUESTIONS,
        TO_NOTES,
        TO_CHAT;

        val data: Bundle = Bundle()
    }
}