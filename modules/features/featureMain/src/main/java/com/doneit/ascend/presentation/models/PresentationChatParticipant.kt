package com.doneit.ascend.presentation.models

import com.doneit.ascend.domain.entity.ImageEntity

data class PresentationChatParticipant(
    val userId: Long,
    val fullName: String?,
    val image: ImageEntity?,
    val isHandRisen: Boolean = false,
    val isMuted: Boolean = false
)