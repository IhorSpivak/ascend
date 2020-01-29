package com.doneit.ascend.domain.entity

data class SocketEventEntity(
    val event: SocketEvent,
    val data: SocketUserEntity
)

data class SocketUserEntity(
    val userId: Long,
    val fullName: String,
    val image: ImageEntity?,
    val isHandRisen: Boolean = false
)