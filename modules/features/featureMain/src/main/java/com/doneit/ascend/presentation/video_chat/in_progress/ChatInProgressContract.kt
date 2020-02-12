package com.doneit.ascend.presentation.video_chat.in_progress

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.StartVideoModel
import com.doneit.ascend.presentation.video_chat.VideoChatActivity
import com.twilio.video.RemoteParticipant
import com.vrgsoft.networkmanager.livedata.SingleLiveEvent
import com.vrgsoft.networkmanager.livedata.SingleLiveManager

interface ChatInProgressContract {
    interface ViewModel : BaseViewModel {
        val isMMConnected: LiveData<Boolean>
        val credentials: LiveData<StartVideoModel>
        val groupInfo: LiveData<GroupEntity>
        val isVideoEnabled: LiveData<Boolean>
        val isAudioEnabled: LiveData<Boolean>
        val switchCameraEvent: SingleLiveManager<Unit>
        val focusedUserId: SingleLiveEvent<String>

        fun onPermissionsRequired(resultCode: VideoChatActivity.ResultStatus)
        fun forceDisconnect()
        fun onOpenOptions()
        fun onConnected(presentParticipants: List<RemoteParticipant>)
        fun onUserConnected(participant: RemoteParticipant)
        fun onUserDisconnected(participant: RemoteParticipant)
        fun onSpeakerChanged(id: String?)
        fun isSpeaker(id: String): Boolean
        fun canFetchMMVideo(): Boolean
    }
}