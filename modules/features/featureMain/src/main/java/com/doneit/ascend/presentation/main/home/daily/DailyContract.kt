package com.doneit.ascend.presentation.main.home.daily

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.group.GroupListWithUser

interface DailyContract {
    interface ViewModel: BaseViewModel {
        val isRefreshing: LiveData<Boolean>
        val groups: LiveData<GroupListWithUser>
        val masterMinds: LiveData<List<MasterMindEntity>>

        fun updateData()
        fun navigateToDailyGroups()
        fun onGroupClick(groupId: Long)
        fun onStartChatClick(groupId: Long)

        fun onAllMasterMindsClick()
        fun openProfile(mmId: Long)
    }

    interface Router {
        fun navigateToGroupList(userId: Long?, groupType: GroupType?, isMyGroups: Boolean?)
        fun navigateToAllMasterMinds()
        fun navigateToGroupInfo(id: Long)
        fun navigateToMMInfo(id: Long)
        fun navigateToVideoChat(groupId: Long)
    }
}