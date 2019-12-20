package com.doneit.ascend.presentation.main.master_mind.list

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedViewModel
import com.doneit.ascend.presentation.main.master_mind.list.common.ListArgs

interface ListContract {
    interface ViewModel : ArgumentedViewModel<ListArgs> {
        val masterMinds: LiveData<PagedList<MasterMindEntity>>

        fun updateData()
        fun openProfile(id: Long)
        fun openGroupList(id: Long)
    }

    interface Router {
        fun openProfile(id: Long)
        fun navigateToGroupList(userId: Long)
    }
}