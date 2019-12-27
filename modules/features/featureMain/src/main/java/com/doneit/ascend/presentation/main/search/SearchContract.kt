package com.doneit.ascend.presentation.main.search

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.SearchEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface SearchContract {
    interface ViewModel: BaseViewModel {
        val groups: LiveData<PagedList<SearchEntity>>

        fun submitRequest(query: String)
        fun goBack()
        fun openGroupList(id: Long)
        fun onMMClick(model: MasterMindEntity)
        fun onGroupClick(model: GroupEntity)
    }

    interface Router {
        fun closeActivity()
        fun navigateToGroupList(userId: Long)
        fun navigateToGroupInfo(model: GroupEntity)
        fun navigateToMMInfo(model: MasterMindEntity)
    }
}