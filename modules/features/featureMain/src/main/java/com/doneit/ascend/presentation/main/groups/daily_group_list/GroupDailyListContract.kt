package com.doneit.ascend.presentation.main.groups.daily_group_list

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedViewModel
import com.doneit.ascend.presentation.main.groups.group_list.GroupListArgs
import com.doneit.ascend.presentation.models.group.GroupListWithUserPaged

interface GroupDailyListContract {
    interface ViewModel : ArgumentedViewModel<GroupListArgs> {
        val groups: LiveData<GroupListWithUserPaged>

        fun onStartChatClick(groupId: Long)
        fun onGroupClick(model: GroupEntity)
        fun backClick()
    }

    interface Router {
        fun navigateToGroupInfo(id: Long)
        fun navigateToVideoChat(groupId: Long)

        fun onBack()
    }
}