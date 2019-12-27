package com.doneit.ascend.presentation.main.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.SearchEntity
import com.doneit.ascend.domain.entity.dto.SearchModel
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.use_case.interactor.search.SearchUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.utils.Constants
import kotlinx.coroutines.launch

class SearchViewModel(
    private val router: SearchContract.Router,
    private val searchUseCase: SearchUseCase
) : BaseViewModelImpl(), SearchContract.ViewModel {

    init {
        viewModelScope.launch {
            val model = SearchModel(
                perPage = Constants.PER_PAGE_COMMON,
                sortColumn = "name",
                sortType = SortType.ASC,
                query = ""
            )

            val result = searchUseCase.getSearchResultPaged(model)
            groups.postValue(result)
        }
    }

    override val groups = MutableLiveData<PagedList<SearchEntity>>()

    override fun submitRequest(query: String) {
        viewModelScope.launch {
            val model = SearchModel(
                perPage = Constants.PER_PAGE_COMMON,
                sortColumn = "name",
                sortType = SortType.ASC,
                query = query
            )

            val result = searchUseCase.getSearchResultPaged(model)
            groups.postValue(result)
        }
    }

    override fun openGroupList(id: Long) {
        router.navigateToGroupList(id)
    }

    override fun onMMClick(model: MasterMindEntity) {
        router.navigateToProfile(model)
    }

    override fun onGroupClick(id: Long) {
        router.navigateToGroupInfo(id)
    }

    override fun goBack() {
        router.closeActivity()
    }
}