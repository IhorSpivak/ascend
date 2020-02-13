package com.doneit.ascend.presentation.video_chat.in_progress.twilio_listeners

import com.twilio.video.RemoteParticipant
import com.twilio.video.RemoteVideoTrackPublication

open class RemoteParticipantMultilistener : RemoteParticipantListener() {

    private val listeners = mutableListOf<RemoteParticipantListener>()

    fun addListener(listener: RemoteParticipantListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: RemoteParticipantListener) {
        listeners.remove(listener)
    }

    override fun onVideoTrackEnabled(
        remoteParticipant: RemoteParticipant,
        remoteVideoTrackPublication: RemoteVideoTrackPublication
    ) {
        listeners.forEach {
            it.onVideoTrackEnabled(remoteParticipant, remoteVideoTrackPublication)
        }
    }

    override fun onVideoTrackDisabled(
        remoteParticipant: RemoteParticipant,
        remoteVideoTrackPublication: RemoteVideoTrackPublication
    ) {
        listeners.forEach {
            it.onVideoTrackDisabled(remoteParticipant, remoteVideoTrackPublication)
        }
    }
}