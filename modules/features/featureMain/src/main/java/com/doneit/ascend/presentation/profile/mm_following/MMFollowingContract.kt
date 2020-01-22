package com.doneit.ascend.presentation.profile.mm_following

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface MMFollowingContract {
    interface ViewModel: BaseViewModel {
        val masterMinds: LiveData<PagedList<MasterMindEntity>>

        fun fetchList()
        fun unfollow(id: Long)
        fun openInfo(entity: MasterMindEntity)
        fun onAddMasterMindClick()
        fun onBackClick()
    }

    interface Router {
        fun navigateToMMInfo(model: MasterMindEntity)
        fun navigateToAddMasterMind()
        fun onBack()
    }
}