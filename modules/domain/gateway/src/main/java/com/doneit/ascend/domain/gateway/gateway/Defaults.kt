package com.doneit.ascend.domain.gateway.gateway

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.dto.BasePagedDTO

fun getDefaultPagedConfig(model: BasePagedDTO): PagedList.Config {
    return PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(model.perPage ?: 10)
        .build()
}