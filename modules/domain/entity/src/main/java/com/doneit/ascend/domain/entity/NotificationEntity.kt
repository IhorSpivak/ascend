package com.doneit.ascend.domain.entity

import java.util.*

data class NotificationEntity(
    val id: Long?,
    val notificationType: String?,
    val groupId: Long?,
    val groupName: String?,
    val createdAt: Date?,
    val updatedAt: Date?,
    val owner: NotificationOwnerEntity?
)