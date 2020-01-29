package com.doneit.ascend.presentation.video_chat.in_progress

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.StartVideoModel
import com.doneit.ascend.presentation.video_chat.VideoChatActivity
import com.vrgsoft.networkmanager.livedata.SingleLiveManager

interface ChatInProgressContract {
    interface ViewModel : BaseViewModel {
        val hasActiveSpeaker: LiveData<Boolean>
        val credentials: LiveData<StartVideoModel>
        val groupInfo: LiveData<GroupEntity>
        val isVideoEnabled: LiveData<Boolean>
        val isAudioEnabled: LiveData<Boolean>
        val isRecordEnabled: LiveData<Boolean>
        val switchCameraEvent: SingleLiveManager<Unit>

        fun onPermissionsRequired(resultCode: VideoChatActivity.ResultStatus)
        fun forceDisconnect()
        fun onOpenOptions()
        fun onSpeakerChanged(id: String)
        fun onUserDisconnected(id: String): Boolean
    }
}