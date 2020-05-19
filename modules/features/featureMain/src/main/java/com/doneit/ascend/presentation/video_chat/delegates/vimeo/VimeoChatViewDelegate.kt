package com.doneit.ascend.presentation.video_chat.delegates.vimeo

import android.view.View
import com.doneit.ascend.presentation.models.StartVideoModel
import com.doneit.ascend.presentation.models.group.PresentationChatParticipant
import com.doneit.ascend.presentation.video_chat.delegates.VideoChatViewDelegate

class VimeoChatViewDelegate(
    private val viewModelDelegate: VimeoChatViewModelDelegate,
    private var placeholder: View?
) : VideoChatViewDelegate {


    override fun startVideo(model: StartVideoModel) {
        //TODO("Not yet implemented")
    }

    override fun clearRenderers() {
        //TODO("Not yet implemented")
    }

    override fun enableVideo(isEnable: Boolean) {
        //TODO("Not yet implemented")
    }

    override fun enableAudio(isEnable: Boolean) {
        //TODO("Not yet implemented")
    }

    override fun onStart() {
        //TODO("Not yet implemented")
    }

    override fun onStop() {
        //TODO("Not yet implemented")
    }

    override fun startVideoDisplay(chatParticipant: PresentationChatParticipant) {
        //TODO("Not yet implemented")
    }

    override fun showPlaceholder() {
        //TODO("Not yet implemented")
    }

    override fun switchCamera() {
        //TODO("Not yet implemented")
    }

}