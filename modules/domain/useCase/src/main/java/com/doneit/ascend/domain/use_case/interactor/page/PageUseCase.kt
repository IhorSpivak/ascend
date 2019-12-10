package com.doneit.ascend.domain.use_case.interactor.page

import com.doneit.ascend.domain.entity.PageEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity

interface PageUseCase {
    suspend fun getPage(pageType: String): ResponseEntity<PageEntity, List<String>>
}