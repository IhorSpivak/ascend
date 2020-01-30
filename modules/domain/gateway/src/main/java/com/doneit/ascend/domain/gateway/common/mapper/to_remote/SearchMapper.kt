package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.entity.dto.GroupStatus
import com.doneit.ascend.domain.entity.dto.SearchModel
import com.doneit.ascend.source.storage.remote.data.request.group.GroupListRequest
import com.doneit.ascend.source.storage.remote.data.request.MasterMindListRequest

fun SearchModel.toGroupRequest(page: Int): GroupListRequest {
    return GroupListRequest(
        page,
        perPage,
        groupSortColumn,
        groupSortType?.toString(),
        query,
        null,
        null,
        GroupStatus.UPCOMING.toString(),
        null,
        null,
        null
    )
}

fun SearchModel.toMasterMindRequest(page: Int): MasterMindListRequest {
    return MasterMindListRequest(
        page,
        perPage,
        mmSortColumn,
        mmSortType?.toString(),
        query,
        null,
        null,
        null
    )
}