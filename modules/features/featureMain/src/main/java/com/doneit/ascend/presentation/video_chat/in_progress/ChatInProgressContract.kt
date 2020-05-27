package com.doneit.ascend.presentation.video_chat.in_progress

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.StartVideoModel
import com.doneit.ascend.presentation.models.group.PresentationChatParticipant
import com.doneit.ascend.presentation.video_chat.VideoChatActivity
import com.doneit.ascend.presentation.video_chat.delegates.twilio.TwilioChatViewModelDelegate
import com.vrgsoft.networkmanager.livedata.SingleLiveManager

interface ChatInProgressContract {
    interface ViewModel : BaseViewModel {
        val currentSpeaker: LiveData<PresentationChatParticipant?>
        val isMMConnected: LiveData<Boolean>
        val groupInfo: LiveData<GroupEntity>
        val isVideoEnabled: LiveData<Boolean>
        val isAudioRecording: LiveData<Boolean>
        //val isAudioEnabled: LiveData<Boolean>

        //VideoChat
        val credentials: LiveData<StartVideoModel>
        val switchCameraEvent: SingleLiveManager<Unit>
        var viewModelDelegate: TwilioChatViewModelDelegate?

        fun onOpenOptions()
        fun onPermissionsRequired(resultCode: VideoChatActivity.ResultStatus)
    }
}