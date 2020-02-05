package com.doneit.ascend.presentation.video_chat.in_progress.user_options.notes

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface NotesContract {
    interface ViewModel : BaseViewModel {
        val groupInfo: LiveData<GroupEntity?>

        fun init(groupId: Long)
        fun onBackClick()
    }

    interface Router {
        fun onBack()
    }
}