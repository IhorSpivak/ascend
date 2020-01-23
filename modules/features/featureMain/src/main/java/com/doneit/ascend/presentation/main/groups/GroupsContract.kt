package com.doneit.ascend.presentation.main.groups

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedViewModel
import com.doneit.ascend.presentation.main.groups.common.GroupsArgs
import com.doneit.ascend.presentation.models.GroupListWithUserPaged

interface GroupsContract {
    interface ViewModel : ArgumentedViewModel<GroupsArgs> {
        val groups: LiveData<GroupListWithUserPaged>
        val groupType: LiveData<String>

        fun backClick()
        fun onStartChatClick(groupId: Long)
        fun onGroupClick(model: GroupEntity)
    }

    interface Router {
        fun onBack()
        fun navigateToGroupInfo(model: GroupEntity)
        fun navigateToVideoChat(groupId: Long)
    }
}