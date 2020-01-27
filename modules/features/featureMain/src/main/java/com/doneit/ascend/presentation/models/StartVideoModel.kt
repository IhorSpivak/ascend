package com.doneit.ascend.presentation.models

import com.doneit.ascend.presentation.video_chat.ChatBehaviour
import com.twilio.video.CameraCapturer

data class StartVideoModel(
    val behaviour: ChatBehaviour,
    val name: String,
    val accessToken: String,
    val camera: CameraCapturer.CameraSource
)