package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.entity.CreateGroupModel
import com.doneit.ascend.source.storage.remote.data.request.CreateGroupRequest

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