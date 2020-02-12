package com.doneit.ascend.presentation.models

import com.doneit.ascend.domain.entity.ImageEntity
import com.doneit.ascend.presentation.utils.Constants.DEFAULT_MODEL_ID
import com.twilio.video.RemoteParticipant

data class PresentationChatParticipant(
    val userId: String = DEFAULT_MODEL_ID.toString(),
    val fullName: String? = null,
    val image: ImageEntity? = null,
    val isHandRisen: Boolean = false,
    val isSpeaker: Boolean = false,
    val remoteParticipant: RemoteParticipant? = null
)