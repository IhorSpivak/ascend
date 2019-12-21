package com.doneit.ascend.domain.entity

data class NotificationEntity(
    val id: Long?,
    val notificationType: String?,
    val groupId: Long?,
    val groupName: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val owner: NotificationOwnerEntity?
)