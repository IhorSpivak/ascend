package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.notification.NotificationEntity
import com.doneit.ascend.domain.entity.notification.NotificationOwnerEntity
import com.doneit.ascend.domain.entity.notification.NotificationType
import com.doneit.ascend.source.storage.local.data.notification.NotificationLocal
import com.doneit.ascend.source.storage.local.data.notification.NotificationOwnerLocal
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
        NotificationType.fromString(notificationType),
        groupId,
        groupName,
        entity_name,
        createdAt?.toDate(),
        updatedAt?.toDate(),
        title,
        owner.toEntity(),
        true//consider request notification as it's reading
    )
}

fun NotificationLocal.toEntity(): NotificationEntity {
    return NotificationEntity(
        id,
        NotificationType.fromString(notificationType),
        groupId,
        groupName,
        entity_name,
        createdAt?.toDate(),
        updatedAt?.toDate(),
        title,
        owner?.toEntity(),
        isRead
    )
}

fun NotificationOwnerLocal.toEntity(): NotificationOwnerEntity {
    return NotificationOwnerEntity(
        id,
        fullName,
        displayName
    )
}