package com.doneit.ascend.presentation.video_chat_webinar.delegate.vimeo

import android.view.View
import androidx.fragment.app.Fragment
import com.doneit.ascend.presentation.models.StartVideoModel

class VimeoChatViewDelegate(
    private val viewModelDelegate: IVimeoChatViewModelDelegate?,
    private var placeholder: View?
) : IVimeoChatViewDelegate{

    var fragment: Fragment? = null

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

    override fun startVideoDisplay() {
        //TODO("Not yet implemented")
    }

    override fun showPlaceholder() {
        //TODO("Not yet implemented")
    }

    override fun switchCamera() {
        //TODO("Not yet implemented")
    }

    override fun startSelfViewVideo(model: StartVideoModel) {
        //TODO("Not yet implemented")
    }
}