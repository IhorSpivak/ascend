package com.doneit.ascend.presentation.models

import com.doneit.ascend.presentation.video_chat.states.ChatRole

sealed class StartVideoModel {
    data class TwilioVideoModel(
        val role: ChatRole,
        val name: String,
        val accessToken: String
    ): StartVideoModel()

    data class VimeoVideoModel(
        val role: ChatRole,
        val chatId: String
    ): StartVideoModel()
}

