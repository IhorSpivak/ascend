package com.doneit.ascend.presentation.profile.mm_following.mm_add

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface MMAddContract {
    interface ViewModel: BaseViewModel {
        val masterMinds: LiveData<PagedList<MasterMindEntity>>

        fun submitRequest(name: String)
        fun onBackClick()
        fun follow(id: Long)
        fun openInfo(entity: MasterMindEntity)
    }

    interface Router {
        fun navigateToMMInfo(id: Long)
        fun onBack()
    }
}