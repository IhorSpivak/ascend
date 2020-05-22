package com.doneit.ascend.presentation.main.home.master_mind

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.main.home.master_mind.filter.FilterContract
import com.doneit.ascend.presentation.models.group.GroupListWithUserPaged

interface MasterMindContract {
    interface ViewModel : BaseViewModel, FilterContract.ViewModel {
        val groups: LiveData<GroupListWithUserPaged>

        fun updateData()
        fun onStartChatClick(groupId: Long, groupType: GroupType)
        fun onGroupClick(groupId: Long)
        fun onFilterClick()
    }

    interface Router : FilterContract.Router {
        fun navigateToGroupInfo(id: Long)
        fun navigateToVideoChat(groupId: Long, groupType: GroupType)
        fun navigateToGroupsFilter()
    }
}