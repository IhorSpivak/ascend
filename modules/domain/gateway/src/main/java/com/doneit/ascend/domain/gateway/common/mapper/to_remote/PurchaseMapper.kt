package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.entity.dto.PurchasesModel
import com.doneit.ascend.source.storage.remote.data.request.PurchasesRequest

fun PurchasesModel.toRequest(page: Int): PurchasesRequest {
    return PurchasesRequest(
        page,
        perPage,
        sortColumn,
        sortType?.toString(),
        createdAtFrom,
        createdAtTo
    )
}