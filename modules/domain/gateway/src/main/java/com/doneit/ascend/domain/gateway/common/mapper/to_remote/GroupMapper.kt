package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.entity.dto.CreateGroupModel
import com.doneit.ascend.domain.entity.dto.GroupListModel
import com.doneit.ascend.source.storage.remote.data.request.CreateGroupRequest
import com.doneit.ascend.source.storage.remote.data.request.GroupListRequest

fun CreateGroupModel.toCreateGroupRequest(): CreateGroupRequest {
    return CreateGroupRequest(
        name,
        description,
        startTime,
        groupType,
        price,
        participants
    )
}

fun GroupListModel.toRequest(userId: Long?): GroupListRequest {
    return GroupListRequest(
        page,
        perPage,
        sortColumn,
        sortType?.toString(),
        name,
        userId,
        groupType?.toString()
    )
}