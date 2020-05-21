package com.doneit.ascend.presentation.video_chat_webinar.preview

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface WebinarChatPreviewContract {
    interface ViewModel : BaseViewModel {
        val groupInfo: LiveData<GroupEntity>

        fun onOpenOptions()
    }
}