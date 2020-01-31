package com.doneit.ascend.presentation.video_chat.preview

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.StartVideoModel

interface ChatPreviewContract {
    interface ViewModel: BaseViewModel {
        val groupInfo: LiveData<GroupEntity>
        val isStartButtonVisible: LiveData<Boolean>
        val credentials: LiveData<StartVideoModel>

        fun onOpenOptions()
        fun onStartGroupClick()
    }
}