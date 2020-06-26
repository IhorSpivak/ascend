package com.doneit.ascend.presentation.video_chat.delegates.twilio

import android.view.View
import androidx.fragment.app.Fragment
import com.doneit.ascend.presentation.models.StartVideoModel
import com.doneit.ascend.presentation.models.group.PresentationChatParticipant
import com.doneit.ascend.presentation.utils.extensions.show
import com.doneit.ascend.presentation.utils.extensions.visible
import com.doneit.ascend.presentation.video_chat.in_progress.twilio_listeners.RemoteParticipantListener
import com.doneit.ascend.presentation.video_chat.in_progress.twilio_listeners.RoomListener
import com.twilio.video.*

class TwilioChatViewDelegate(
    private val viewModelDelegate: TwilioChatViewModelDelegate,
    private var placeholder: View?
) : ITwilioChatViewDelegate {

    private val roomListener = getActivityRoomListener()
    //region video chat
    private val audioCodec: AudioCodec by lazy { OpusCodec() }
    private val videoCodec: VideoCodec by lazy { Vp8Codec() }
    //endregion

    var fragment: Fragment? = null
    var videoView: VideoTextureView? = null
    var configureAudio: (Boolean) -> Unit = {}

    private var isPlaceholderAllowed = true
        set(value) {
            field = value
            placeholder?.show()//enable mm icon and group name
        }

    override fun startVideo(model: StartVideoModel) {
        if (model !is StartVideoModel.TwilioVideoModel || fragment == null) return
        viewModelDelegate.startVideo(fragment!!, model, audioCodec, videoCodec)
    }

    override fun clearRenderers() {
        viewModelDelegate.room?.remoteParticipants?.forEach { roomParticipant ->
            videoView?.let {
                roomParticipant.videoTracks.firstOrNull()?.videoTrack?.removeRenderer(it)
            }
        }
    }

    override fun enableVideo(isEnable: Boolean) {
        viewModelDelegate.localVideoTrack?.enable(isEnable)
    }

    override fun enableAudio(isEnable: Boolean) {
        viewModelDelegate.localAudioTrack?.enable(isEnable)
    }

    override fun onStart() {
        viewModelDelegate.roomListener.addListener(roomListener)
    }

    override fun onStop() {
        viewModelDelegate.roomListener.removeListener(roomListener)
    }

    private fun getActivityRoomListener(): RoomListener {
        return object : RoomListener() {
            override fun onConnectFailure(room: Room, twilioException: TwilioException) {
                configureAudio(false)
            }
        }
    }

    override fun startVideoDisplay(chatParticipant: PresentationChatParticipant) {
        chatParticipant.setPrimaryVideoListener(getSpeakerListener())
        videoView?.let {
            val track = chatParticipant.remoteParticipant?.videoTracks?.firstOrNull()?.videoTrack
            if (track != null) {
                track.addRenderer(it)
                showVideo()
            } else {
                showPlaceholder()
            }
        }
    }

    override fun startSelfVideoDisplay() {
        videoView?.let {
            val track = viewModelDelegate.localVideoTrack
            if (track != null) {
                track.addRenderer(it)
                showVideo()
            } else {
                showPlaceholder()
            }
        }
    }

    private fun getSpeakerListener(): RemoteParticipantListener {
        return object : RemoteParticipantListener() {

            override fun onVideoTrackEnabled(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication
            ) {
                showVideo()
            }

            override fun onVideoTrackDisabled(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication
            ) {
                showPlaceholder()
            }
        }
    }

    override fun showPlaceholder() {
        placeholder?.visible(isPlaceholderAllowed)
        videoView?.visible(false)
    }

    private fun showVideo() {
        placeholder?.visible(false)
        videoView?.visible(true)
    }

    override fun switchCamera() {
        viewModelDelegate.switchCamera()
    }
}