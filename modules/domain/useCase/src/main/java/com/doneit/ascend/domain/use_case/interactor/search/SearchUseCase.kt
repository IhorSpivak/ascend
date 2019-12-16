package com.doneit.ascend.domain.use_case.interactor.search

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.SearchEntity
import com.doneit.ascend.domain.entity.dto.SearchModel

interface SearchUseCase  {
    suspend fun getSearchResultPaged(model: SearchModel): PagedList<SearchEntity>
}