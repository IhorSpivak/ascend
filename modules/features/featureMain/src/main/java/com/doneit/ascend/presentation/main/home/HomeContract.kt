package com.doneit.ascend.presentation.main.home

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.domain.entity.dto.GroupType
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.main.home.common.TabAdapter

interface HomeContract {
    interface ViewModel : BaseViewModel {
        val isRefreshing: LiveData<Boolean>
        val user: LiveData<UserEntity?>
        val masterMinds: LiveData<List<MasterMindEntity>>
        val tabAdapter: LiveData<TabAdapter>

        fun navigateToGroupList()
        fun updateData()
        fun updateMasterMinds()
        fun onSearchClick()
        fun onAllMasterMindsClick()
        fun openProfile(model: MasterMindEntity)
        fun onNotificationClick()
    }

    interface Router {
        fun navigateToGroupList(groupType: GroupType?, isMyGroups: Boolean?, isAllGroups: Boolean)
        fun navigateToSearch()
        fun navigateToAllMasterMinds()
        fun navigateToGroupInfo(model: GroupEntity)
        fun openProfile(model: MasterMindEntity)
        fun navigateToNotifications()
    }
}