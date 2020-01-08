package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.entity.dto.CreateGroupModel
import com.doneit.ascend.domain.entity.dto.GroupListModel
import com.doneit.ascend.domain.entity.dto.SubscribeGroupModel
import com.doneit.ascend.source.storage.remote.data.request.CreateGroupRequest
import com.doneit.ascend.source.storage.remote.data.request.GroupListRequest
import com.doneit.ascend.source.storage.remote.data.request.SubscribeGroupRequest

fun CreateGroupModel.toCreateGroupRequest(): CreateGroupRequest {
    return CreateGroupRequest(
        name,
        description,
        startTime.toRemoteString(),
        groupType,
        price * 100,
        participants,
        days,
        meetingsCount
    )
}

fun GroupListModel.toRequest(): GroupListRequest {
    return GroupListRequest(
        page,
        perPage,
        sortColumn,
        sortType?.toString(),
        name,
        userId,
        groupType?.toString(),
        myGroups
    )
}

fun GroupListModel.toRequest(currPage: Int): GroupListRequest {
    return GroupListRequest(
        currPage,
        perPage,
        sortColumn,
        sortType?.toString(),
        name,
        userId,
        groupType?.toString(),
        myGroups
    )
}

fun SubscribeGroupModel.toRequest(): SubscribeGroupRequest {
    return SubscribeGroupRequest(
        paymentSourceId,
        paymentSourceType.toString()
    )
}