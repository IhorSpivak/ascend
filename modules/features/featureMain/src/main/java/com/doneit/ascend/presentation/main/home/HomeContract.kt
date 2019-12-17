package com.doneit.ascend.presentation.main.home

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.domain.entity.dto.GroupType
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface HomeContract {
    interface ViewModel : BaseViewModel {
        val isRefreshing: LiveData<Boolean>
        val user: LiveData<UserEntity?>
        val masterMinds: LiveData<List<MasterMindEntity>>

        fun navigateToGroupList()
        fun updateData()
        fun updateMasterMinds()
        fun onSearchClick()
        fun onAllMasterMindsClick()
    }

    interface Router {
        fun navigateToGroupList(groupType: GroupType?, isMyGroups: Boolean?, isAllGroups: Boolean)
        fun navigateToSearch()
        fun navigateToAllMasterMinds()
        fun navigateToGroupInfo(id: Long)
    }
}