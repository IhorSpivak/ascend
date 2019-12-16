package com.doneit.ascend.domain.use_case.interactor.search

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.SearchEntity
import com.doneit.ascend.domain.entity.dto.SearchModel
import com.doneit.ascend.domain.use_case.gateway.ISearchGateway

internal class SearchInteractor(
    private val searchGateway: ISearchGateway
) : SearchUseCase {

    override suspend fun getSearchResultPaged(model: SearchModel): PagedList<SearchEntity> {
        return  searchGateway.getSearchResultPaged(model)
    }
}