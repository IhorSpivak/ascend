package com.doneit.ascend.presentation.main.search

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.dto.GroupListModel
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchViewModel(
    private val router: SearchContract.Router,
    private val groupUseCase: GroupUseCase
) : BaseViewModelImpl(), SearchContract.ViewModel {

    init {
        GlobalScope.launch {

            val model = GroupListModel(
                page = 1,
                perPage = 5,
                sortColumn = "name",
                sortType = SortType.ASC,
                name = "ggg",
                myGroups = true
            )

            val result = groupUseCase.getGroupListPaged(model)
            groups.postValue(result)
        }
    }

    override val groups = MutableLiveData<PagedList<GroupEntity>>()

    override fun goBack() {
        router.closeActivity()
    }
}