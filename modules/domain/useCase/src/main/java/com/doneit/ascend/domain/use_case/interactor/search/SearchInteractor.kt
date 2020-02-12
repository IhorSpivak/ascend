package com.doneit.ascend.domain.use_case.interactor.search

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.SearchEntity
import com.doneit.ascend.domain.entity.dto.SearchDTO
import com.doneit.ascend.domain.use_case.gateway.ISearchGateway

internal class SearchInteractor(
    private val searchGateway: ISearchGateway
) : SearchUseCase {

    override suspend fun getSearchResultPaged(dto: SearchDTO): PagedList<SearchEntity> {
        return  searchGateway.getSearchResultPaged(dto)
    }
}