package com.doneit.ascend.presentation.video_chat_webinar.delegate.vimeo

import com.doneit.ascend.presentation.models.StartVideoModel

interface IVimeoChatViewDelegate {
    fun startVideo(model: StartVideoModel)
    fun clearRenderers()
    fun enableVideo(isEnable: Boolean)
    fun enableAudio(isEnable: Boolean)
    fun onStart()
    fun onStop()
    fun startVideoDisplay()
    fun showPlaceholder()
    fun switchCamera()
    fun startSelfViewVideo(model: StartVideoModel)
}