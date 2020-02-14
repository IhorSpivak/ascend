package com.doneit.ascend.domain.entity.notification

import java.util.*

data class NotificationEntity(
    val id: Long?,
    val notificationType: NotificationType,
    val groupId: Long?,
    val groupName: String?,
    val createdAt: Date?,
    val updatedAt: Date?,
    val title: String,
    val owner: NotificationOwnerEntity?,
    val isRead: Boolean = false
)