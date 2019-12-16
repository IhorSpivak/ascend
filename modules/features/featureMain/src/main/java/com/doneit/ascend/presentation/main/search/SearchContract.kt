package com.doneit.ascend.presentation.main.search

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.SearchEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface SearchContract {
    interface ViewModel: BaseViewModel {
        val groups: LiveData<PagedList<SearchEntity>>

        fun submitRequest(query: String)
        fun goBack()
    }

    interface Router {
        fun closeActivity()
    }
}