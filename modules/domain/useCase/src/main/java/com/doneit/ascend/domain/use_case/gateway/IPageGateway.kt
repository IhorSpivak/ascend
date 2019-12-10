package com.doneit.ascend.domain.use_case.gateway

import com.doneit.ascend.domain.entity.PageEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity

interface IPageGateway {
    suspend fun getPage(pageType: String): ResponseEntity<PageEntity, List<String>>
}