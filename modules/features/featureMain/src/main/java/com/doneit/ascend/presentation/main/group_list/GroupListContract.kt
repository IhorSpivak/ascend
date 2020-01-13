package com.doneit.ascend.presentation.main.group_list

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedViewModel
import com.doneit.ascend.presentation.main.group_list.common.GroupListArgs
import com.doneit.ascend.presentation.models.GroupListWithUserPaged

interface GroupListContract {
    interface ViewModel : ArgumentedViewModel<GroupListArgs> {
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