package com.doneit.ascend.presentation.main.home

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.domain.entity.group.GroupType
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
        fun onAllMasterMindsClick()
        fun openProfile(model: MasterMindEntity)
    }

    interface Router {
        fun navigateToGroupList(userId: Long?, groupType: GroupType?, isMyGroups: Boolean?)
        fun navigateToAllMasterMinds()
        fun navigateToGroupInfo(id: Long)
        fun navigateToMMInfo(id: Long)
        fun navigateToVideoChat(groupId: Long)
    }
}