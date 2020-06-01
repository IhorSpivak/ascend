package com.doneit.ascend.presentation.video_chat_webinar.delegate.vimeo

import com.doneit.ascend.presentation.models.StartWebinarVideoModel

interface IVimeoChatViewDelegate {
    fun startVideo(model: StartWebinarVideoModel)
    fun clearRenderers()
    fun enableVideo(isEnable: Boolean)
    fun enableAudio(isEnable: Boolean)
    fun onStart()
    fun onStop()
    fun startVideoDisplay()
    fun showPlaceholder()
    fun switchCamera()
    fun startSelfViewVideo(model: StartWebinarVideoModel)
}