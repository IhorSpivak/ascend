package com.doneit.ascend.presentation.video_chat.in_progress.twilio_listeners

import com.twilio.video.RemoteParticipant
import com.twilio.video.Room
import com.twilio.video.TwilioException

open class RoomListener : Room.Listener {
    override fun onRecordingStopped(room: Room) {

    }

    override fun onParticipantDisconnected(
        room: Room,
        remoteParticipant: RemoteParticipant
    ) {
    }

    override fun onRecordingStarted(room: Room) {
    }

    override fun onConnectFailure(room: Room, twilioException: TwilioException) {
    }

    override fun onReconnected(room: Room) {
    }

    override fun onParticipantConnected(room: Room, remoteParticipant: RemoteParticipant) {
    }

    override fun onConnected(room: Room) {
    }

    override fun onDisconnected(room: Room, twilioException: TwilioException?) {
    }

    override fun onReconnecting(room: Room, twilioException: TwilioException) {
    }
}