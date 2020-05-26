package com.doneit.ascend.presentation.video_chat_webinar.in_progress

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.StartWebinarVideoModel
import com.doneit.ascend.presentation.video_chat.VideoChatActivity
import com.vrgsoft.networkmanager.livedata.SingleLiveManager


interface WebinarVideoChatInProgressContract {
    interface ViewModel : BaseViewModel {
        val groupInfo: LiveData<GroupEntity>
        val isVideoEnabled: LiveData<Boolean>
        val isAudioRecording: LiveData<Boolean>
        val isQuestionSent: LiveData<Boolean>
        val isMMConnected: LiveData<Boolean>
        val isVisitor: LiveData<Boolean>

        //VideoChat
        val credentials: MutableLiveData<StartWebinarVideoModel>
        val switchCameraEvent: SingleLiveManager<Unit>

        fun onOpenOptions()
        fun createQuestion(question: String)
        fun onPermissionsRequired(resultCode: VideoChatActivity.ResultStatus)
    }
}