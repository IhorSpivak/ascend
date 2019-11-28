package com.doneit.ascend.domain.use_case.gateway

import com.doneit.ascend.domain.entity.PageEntity
import com.doneit.ascend.domain.entity.common.RequestEntity

interface IPageGateway {
    suspend fun getPage(pageType: String): RequestEntity<PageEntity, List<String>>
}