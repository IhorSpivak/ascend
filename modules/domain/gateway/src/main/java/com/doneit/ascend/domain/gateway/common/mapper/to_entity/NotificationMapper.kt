package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.NotificationEntity
import com.doneit.ascend.domain.entity.NotificationOwnerEntity
import com.doneit.ascend.source.storage.remote.data.response.NotificationOwnerResponse
import com.doneit.ascend.source.storage.remote.data.response.NotificationResponse

fun NotificationOwnerResponse.toEntity(): NotificationOwnerEntity {
    return NotificationOwnerEntity(
        id,
        fullName,
        displayName
    )
}

fun NotificationResponse.toEntity(): NotificationEntity {
    return NotificationEntity(
        id,
        notificationType,
        groupId,
        groupName,
        createdAt?.toDate(),
        updatedAt?.toDate(),
        owner.toEntity()
    )
}