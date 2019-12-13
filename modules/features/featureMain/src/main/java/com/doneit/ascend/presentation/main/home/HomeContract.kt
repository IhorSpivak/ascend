package com.doneit.ascend.presentation.main.home

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.domain.entity.dto.GroupType
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.vrgsoft.networkmanager.livedata.SingleLiveEvent
import kotlinx.coroutines.Job

interface HomeContract {
    interface ViewModel : BaseViewModel {
        val isRefreshing: LiveData<Boolean>
        val user: LiveData<UserEntity?>
        val masterMinds: LiveData<List<MasterMindEntity>>

        fun navigateToGroupList()
        fun updateData()
        fun updateMasterMinds()
    }

    interface Router {
        fun navigateToGroupList(groupType: GroupType)
    }
}