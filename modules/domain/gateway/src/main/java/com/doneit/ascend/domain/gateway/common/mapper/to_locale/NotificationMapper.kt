package com.doneit.ascend.domain.gateway.common.mapper.to_locale

import com.doneit.ascend.domain.entity.notification.NotificationEntity
import com.doneit.ascend.domain.entity.notification.NotificationOwnerEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRemoteString
import com.doneit.ascend.source.storage.local.data.notification.NotificationLocal
import com.doneit.ascend.source.storage.local.data.notification.NotificationOwnerLocal

fun NotificationEntity.toLocal(): NotificationLocal {
    return NotificationLocal(
        id,
        notificationType.toString(),
        groupId,
        groupName,
        createdAt?.toRemoteString(),
        updatedAt?.toRemoteString(),
        owner?.toLocal(),
        isRead
    )
}

fun NotificationOwnerEntity.toLocal(): NotificationOwnerLocal {
    return NotificationOwnerLocal(
        id,
        fullName,
        displayName
    )
}