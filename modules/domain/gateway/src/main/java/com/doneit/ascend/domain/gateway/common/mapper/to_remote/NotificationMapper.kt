package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.entity.dto.NotificationListDTO
import com.doneit.ascend.source.storage.remote.data.request.NotificationListRequest

fun NotificationListDTO.toRequest(): NotificationListRequest {
    return NotificationListRequest(
        page,
        perPage,
        sortColumn,
        sortType?.toString(),
        notificationType,
        createdAtFrom,
        createdAtTo,
        updatedAtFrom,
        updatedAtTo
    )
}

fun NotificationListDTO.toRequest(page: Int): NotificationListRequest {
    return NotificationListRequest(
        page,
        perPage,
        sortColumn,
        sortType?.toString(),
        notificationType,
        createdAtFrom,
        createdAtTo,
        updatedAtFrom,
        updatedAtTo
    )
}