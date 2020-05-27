package com.doneit.ascend.presentation.video_chat_webinar.preview

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.StartWebinarVideoModel
import com.doneit.ascend.presentation.video_chat_webinar.WebinarVideoChatActivity

interface WebinarChatPreviewContract {
    interface ViewModel : BaseViewModel {
        val groupInfo: LiveData<GroupEntity>
        val isStartButtonVisible: LiveData<Boolean>
        val credentials: LiveData<StartWebinarVideoModel>

        fun onOpenOptions()
        fun onStartGroupClick()
        fun onPermissionsRequired(resultCode: WebinarVideoChatActivity.ResultStatus)
    }
}