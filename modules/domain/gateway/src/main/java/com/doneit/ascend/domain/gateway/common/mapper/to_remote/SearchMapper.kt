package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.entity.dto.SearchDTO
import com.doneit.ascend.domain.entity.group.GroupStatus
import com.doneit.ascend.source.storage.remote.data.request.MasterMindListRequest
import com.doneit.ascend.source.storage.remote.data.request.group.GroupListRequest

fun SearchDTO.toGroupRequest(page: Int): GroupListRequest {
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
        null,
        null,
        null,
        null,
        null,
        null
    )
}

fun SearchDTO.toMasterMindRequest(page: Int): MasterMindListRequest {
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