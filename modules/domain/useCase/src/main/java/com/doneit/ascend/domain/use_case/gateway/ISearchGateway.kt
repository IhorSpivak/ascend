package com.doneit.ascend.domain.use_case.gateway

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.SearchEntity
import com.doneit.ascend.domain.entity.dto.SearchModel

interface ISearchGateway {
    suspend fun getSearchResultPaged(searchRequest: SearchModel): PagedList<SearchEntity>
}