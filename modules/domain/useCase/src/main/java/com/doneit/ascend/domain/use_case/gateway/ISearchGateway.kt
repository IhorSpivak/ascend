package com.doneit.ascend.domain.use_case.gateway

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.SearchEntity
import com.doneit.ascend.domain.entity.dto.SearchDTO

interface ISearchGateway {
    suspend fun getSearchResultPaged(searchRequest: SearchDTO): PagedList<SearchEntity>
}