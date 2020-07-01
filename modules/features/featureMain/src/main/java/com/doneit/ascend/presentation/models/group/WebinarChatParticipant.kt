package com.doneit.ascend.presentation.models.group

import com.doneit.ascend.domain.entity.ImageEntity
import com.doneit.ascend.presentation.utils.Constants

data class WebinarChatParticipant(
    val userId: String = Constants.DEFAULT_MODEL_ID.toString(),
    val fullName: String? = null,
    val image: ImageEntity? = null,
    val isConnected: Boolean
)