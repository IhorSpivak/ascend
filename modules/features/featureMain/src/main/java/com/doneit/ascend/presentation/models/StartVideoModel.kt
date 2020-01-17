package com.doneit.ascend.presentation.models

import com.twilio.video.CameraCapturer

data class StartVideoModel(
    val isTranslator: Boolean,
    val name: String,
    val accessToken: String,
    val camera: CameraCapturer.CameraSource
)