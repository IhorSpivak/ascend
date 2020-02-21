package com.doneit.ascend.presentation.main.home.master_mind

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.group.GroupListWithUserPaged

interface MasterMindContract {
    interface ViewModel: BaseViewModel {
        val groups: LiveData<GroupListWithUserPaged>

        fun updateData()
        fun onStartChatClick(groupId: Long)
        fun onGroupClick(groupId: Long)
    }

    interface Router {
        fun navigateToGroupInfo(id: Long)
        fun navigateToVideoChat(groupId: Long)
    }
}