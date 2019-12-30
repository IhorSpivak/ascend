package com.doneit.ascend.presentation.profile.mm_followed

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface MMFollowedContract {
    interface ViewModel: BaseViewModel {
        val masterMinds: LiveData<PagedList<MasterMindEntity>>

        fun fetchList()
        fun unfollow(id: Long)
        fun onAddMasterMindClick()
        fun onBackClick()
    }

    interface Router {
        fun navigateToAddMasterMind()
        fun goBack()
    }
}