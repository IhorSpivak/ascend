package com.doneit.ascend.domain.entity

data class SocketEventEntity(
    val event: SocketEvent,
    val userId: Long,
    val fullName: String,
    val image: ImageEntity
)