package com.doneit.ascend.presentation.video_chat.preview

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.StartVideoModel
import com.doneit.ascend.presentation.video_chat.VideoChatActivity
import com.vrgsoft.networkmanager.livedata.SingleLiveManager

interface ChatPreviewContract {
    interface ViewModel: BaseViewModel {
        val groupInfo: LiveData<GroupEntity>
        val isStartButtonVisible: LiveData<Boolean>
        val credentials: LiveData<StartVideoModel>
        val switchCameraEvent: SingleLiveManager<Unit>

        fun onOpenOptions()
        fun onStartGroupClick()
        fun onPermissionsRequired(resultCode: VideoChatActivity.ResultStatus)
    }
}