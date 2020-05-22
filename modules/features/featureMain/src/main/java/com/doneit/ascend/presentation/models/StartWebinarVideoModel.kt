package com.doneit.ascend.presentation.models

import com.doneit.ascend.presentation.video_chat.states.ChatRole

data class StartWebinarVideoModel(
    val role: ChatRole,
    val chatId: Int
)