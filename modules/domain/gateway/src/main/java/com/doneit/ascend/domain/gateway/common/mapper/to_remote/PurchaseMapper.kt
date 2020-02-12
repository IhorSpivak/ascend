package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.entity.dto.PurchasesDTO
import com.doneit.ascend.source.storage.remote.data.request.PurchasesRequest

fun PurchasesDTO.toRequest(page: Int): PurchasesRequest {
    return PurchasesRequest(
        page,
        perPage,
        sortColumn,
        sortType?.toString(),
        createdAtFrom?.toRemoteString(),
        createdAtTo?.toRemoteString()
    )
}