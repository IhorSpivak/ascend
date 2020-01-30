package com.doneit.ascend.presentation.models

import com.doneit.ascend.presentation.video_chat.states.ChatRole
import com.twilio.video.CameraCapturer

data class StartVideoModel(
    val role: ChatRole,
    val name: String,
    val accessToken: String,
    val camera: CameraCapturer.CameraSource
)