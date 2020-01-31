package com.doneit.ascend.presentation.video_chat.in_progress

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.StartVideoModel
import com.doneit.ascend.presentation.video_chat.VideoChatActivity
import com.vrgsoft.networkmanager.livedata.SingleLiveEvent
import com.vrgsoft.networkmanager.livedata.SingleLiveManager

interface ChatInProgressContract {
    interface ViewModel : BaseViewModel {
        val isMMConnected: LiveData<Boolean>
        val credentials: LiveData<StartVideoModel>
        val groupInfo: LiveData<GroupEntity>
        val isVideoEnabled: LiveData<Boolean>
        val isAudioEnabled: LiveData<Boolean>
        val isRecordEnabled: LiveData<Boolean>
        val switchCameraEvent: SingleLiveManager<Unit>
        val focusedUserId: SingleLiveEvent<String>

        fun onPermissionsRequired(resultCode: VideoChatActivity.ResultStatus)
        fun forceDisconnect()
        fun onOpenOptions()
        fun onUserConnected(id: String)
        fun onUserDisconnected(id: String)
        fun onSpeakerChanged(id: String?)
        fun isSpeaker(id: String): Boolean
        fun canFetchMMVideo(): Boolean
    }
}