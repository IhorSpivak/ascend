package com.doneit.ascend.presentation.main.search

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
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
        fun onGroupClick(id: Long)
    }

    interface Router {
        fun closeActivity()
        fun navigateToGroupList(userId: Long)
        fun navigateToGroupInfo(id: Long)
        fun navigateToProfile(model: MasterMindEntity)
    }
}