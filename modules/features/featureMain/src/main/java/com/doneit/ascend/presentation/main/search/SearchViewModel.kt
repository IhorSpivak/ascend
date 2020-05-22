package com.doneit.ascend.presentation.main.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.SearchEntity
import com.doneit.ascend.domain.entity.dto.SearchDTO
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.use_case.interactor.search.SearchUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.utils.Constants
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class SearchViewModel(
    private val router: SearchContract.Router,
    private val searchUseCase: SearchUseCase
) : BaseViewModelImpl(), SearchContract.ViewModel {

    init {
        viewModelScope.launch {
            val model = SearchDTO(
                perPage = Constants.PER_PAGE_COMMON,
                mmSortColumn = MasterMindEntity.FULL_NAME_KEY,
                mmSortType = SortType.ASC,
                groupSortColumn = GroupEntity.START_TIME_KEY,
                groupSortType = SortType.ASC,
                query = ""
            )

            val result = searchUseCase.getSearchResultPaged(model)
            groups.postValue(result)
        }
    }

    override val groups = MutableLiveData<PagedList<SearchEntity>>()

    override fun submitRequest(query: String) {
        viewModelScope.launch {
            val model = SearchDTO(
                perPage = Constants.PER_PAGE_COMMON,
                mmSortColumn = "fullName",
                mmSortType = SortType.ASC,
                query = query
            )

            val result = searchUseCase.getSearchResultPaged(model)
            groups.postValue(result)
        }
    }

    override fun openGroupList(mm: MasterMindEntity) {
        router.navigateToGroupList(mm.id, null, null, mm.fullName)
    }

    override fun onMMClick(model: MasterMindEntity) {
        router.navigateToMMInfo(model.id)
    }

    override fun onGroupClick(model: GroupEntity) {
        router.navigateToGroupInfo(model.id)
    }

    override fun onStartChatClick(groupId: Long, groupType: GroupType) {
        router.navigateToVideoChat(groupId, groupType)
    }

    override fun goBack() {
        router.onBack()
    }
}