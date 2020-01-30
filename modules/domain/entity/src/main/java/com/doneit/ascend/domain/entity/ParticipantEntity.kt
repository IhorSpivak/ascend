package com.doneit.ascend.domain.entity

data class ParticipantEntity(
    val id: Long,
    val fullName: String,
    val image: ImageEntity?,
    val isHandRisen: Boolean,
    val isConnected: Boolean,
    val isVisited: Boolean,
    val isBlocked: Boolean,
    val isSpeaker: Boolean
)