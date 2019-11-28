package com.doneit.ascend.domain.use_case.interactor.page

import com.doneit.ascend.domain.entity.PageEntity
import com.doneit.ascend.domain.entity.common.RequestEntity

interface PageUseCase {
    suspend fun getPage(pageType: String): RequestEntity<PageEntity, List<String>>
}