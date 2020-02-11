package com.doneit.ascend.presentation.video_chat.in_progress.user_options.notes

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface NotesContract {
    interface ViewModel : BaseViewModel {
        val groupInfo: LiveData<GroupEntity?>
        val navigation: LiveData<Navigation>

        fun init(groupId: Long)
        fun update(newContent: String)
        fun onBackClick()
    }

    interface Router {
        fun onBack()
    }

    enum class Navigation {
        BACK
    }
}