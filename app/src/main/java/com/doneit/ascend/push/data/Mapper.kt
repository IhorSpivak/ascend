package com.doneit.ascend.push.data

import com.doneit.ascend.domain.entity.notification.NotificationEntity
import com.doneit.ascend.domain.entity.notification.NotificationType

fun PushEvent.toEntity(): NotificationEntity {
    return NotificationEntity(
        -1,
        NotificationType.fromString(type),
        groupId,
        title,
        null,
        null,
        "",
        null,
        false
    )
}