package com.doneit.ascend.presentation.main.group_info

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.GroupDetailsEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface GroupInfoContract {
    interface ViewModel: BaseViewModel {
        val group: LiveData<GroupDetailsEntity>

        fun onBackPressed()
        fun loadData(groupId: Long)
        fun join()
        fun subscribe()
        fun deleteGroup()
    }

    interface Router {
        fun closeActivity()
    }
}