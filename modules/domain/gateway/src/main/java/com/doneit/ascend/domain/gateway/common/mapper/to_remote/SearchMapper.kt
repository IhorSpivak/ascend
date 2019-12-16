package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.entity.dto.SearchModel
import com.doneit.ascend.source.storage.remote.data.request.GroupListRequest
import com.doneit.ascend.source.storage.remote.data.request.MasterMindListRequest

fun SearchModel.toGroupRequest(page: Int): GroupListRequest {
    return GroupListRequest(
        page,
        perPage,
        sortColumn,
        sortType?.toString(),
        query,
        null,
        null,
        null
    )
}

fun SearchModel.toMasterMindRequest(page: Int): MasterMindListRequest {
    return MasterMindListRequest(
        page,
        perPage,
        sortColumn,
        sortType?.toString(),
        query,
        null,
        null,
        null
    )
}