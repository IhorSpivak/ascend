package com.doneit.ascend.presentation.main.search

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface SearchContract {
    interface ViewModel: BaseViewModel {
        val groups: LiveData<PagedList<GroupEntity>>

        fun goBack()
    }

    interface Router {
        fun closeActivity()
    }
}