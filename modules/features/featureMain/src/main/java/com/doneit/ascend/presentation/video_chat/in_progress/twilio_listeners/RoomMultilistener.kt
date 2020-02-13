package com.doneit.ascend.presentation.video_chat.in_progress.twilio_listeners

import com.twilio.video.RemoteParticipant
import com.twilio.video.Room
import com.twilio.video.TwilioException

class RoomMultilistener : RoomListener() {
    private val listeners = mutableListOf<RoomListener>()

    fun addListener(listener: RoomListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: RoomListener) {
        listeners.remove(listener)
    }

    override fun onConnected(room: Room) {
        listeners.forEach {
            it.onConnected(room)
        }
    }

    override fun onConnectFailure(room: Room, twilioException: TwilioException) {
        listeners.forEach {
            it.onConnectFailure(room, twilioException)
        }
    }

    override fun onParticipantConnected(room: Room, remoteParticipant: RemoteParticipant) {
        listeners.forEach {
            it.onParticipantConnected(room, remoteParticipant)
        }
    }

    override fun onParticipantDisconnected(
        room: Room,
        remoteParticipant: RemoteParticipant
    ) {
        listeners.forEach {
            it.onParticipantDisconnected(room, remoteParticipant)
        }
    }

    override fun onDominantSpeakerChanged(
        room: Room,
        remoteParticipant: RemoteParticipant?
    ) {
        listeners.forEach {
            it.onDominantSpeakerChanged(room, remoteParticipant)
        }
    }
}