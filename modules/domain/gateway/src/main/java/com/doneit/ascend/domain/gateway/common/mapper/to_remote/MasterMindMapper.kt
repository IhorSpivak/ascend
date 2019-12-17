package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.entity.dto.MasterMindListModel
import com.doneit.ascend.source.storage.remote.data.request.MasterMindListRequest

fun MasterMindListModel.toRequest(): MasterMindListRequest {
    return MasterMindListRequest(
        page,
        perPage,
        sortColumn,
        sortType?.toString(),
        fullName,
        displayName,
        followed,
        rated
    )
}

fun MasterMindListModel.toRequest(page: Int): MasterMindListRequest {
    return MasterMindListRequest(
        page,
        perPage,
        sortColumn,
        sortType?.toString(),
        fullName,
        displayName,
        followed,
        rated
    )
}