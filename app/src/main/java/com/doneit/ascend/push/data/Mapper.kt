package com.doneit.ascend.push.data

import com.doneit.ascend.domain.entity.notification.NotificationEntity
import com.doneit.ascend.domain.entity.notification.NotificationType
import com.google.firebase.messaging.RemoteMessage

fun RemoteMessage.toEntity(): NotificationEntity {
    return NotificationEntity(
        -1,
        NotificationType.fromString(this.data.getValue("type")),
        this.data.getValue("group_id").toLong(),
        this.notification?.title!!,
        null,
        null,
        "",
        null,
        false
    )
}