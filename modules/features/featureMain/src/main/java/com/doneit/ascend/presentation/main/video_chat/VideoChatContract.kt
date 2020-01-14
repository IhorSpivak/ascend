package com.doneit.ascend.presentation.main.video_chat

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface VideoChatContract {
    interface ViewModel : BaseViewModel {
        val groupInfo: LiveData<GroupEntity>
        val timer: LiveData<String>

        fun init(groupId: Long)
        fun onBackClick()
    }

    interface Router {
        fun onBack()
        fun navigateToPreview()
        fun navigateToChatInProgress()
        fun navigateToChatFinishScreen()
        fun navigateToPermissionsRequiredDialog(resultCode: VideoChatActivity.ResultStatus)
    }
}