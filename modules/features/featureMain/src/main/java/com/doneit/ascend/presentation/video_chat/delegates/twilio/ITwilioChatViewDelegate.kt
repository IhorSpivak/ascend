package com.doneit.ascend.presentation.video_chat.delegates.twilio

import com.doneit.ascend.presentation.models.StartVideoModel
import com.doneit.ascend.presentation.models.group.PresentationChatParticipant

interface ITwilioChatViewDelegate {
    fun startVideo(model: StartVideoModel)
    fun clearRenderers()
    fun enableVideo(isEnable: Boolean)
    fun enableAudio(isEnable: Boolean)
    fun onStart()
    fun onStop()
    fun startVideoDisplay(chatParticipant: PresentationChatParticipant)
    fun startSelfVideoDisplay()
    fun showPlaceholder()
    fun switchCamera()
}