package com.doneit.ascend.domain.use_case.interactor.search

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.SearchEntity
import com.doneit.ascend.domain.entity.dto.SearchDTO

interface SearchUseCase  {
    suspend fun getSearchResultPaged(dto: SearchDTO): PagedList<SearchEntity>
}