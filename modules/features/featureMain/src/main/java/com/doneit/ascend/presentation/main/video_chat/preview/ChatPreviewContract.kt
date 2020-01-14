package com.doneit.ascend.presentation.main.video_chat.preview

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface ChatPreviewContract {
    interface ViewModel: BaseViewModel {
        val groupInfo: LiveData<GroupEntity>

        fun startGroup()
    }
}