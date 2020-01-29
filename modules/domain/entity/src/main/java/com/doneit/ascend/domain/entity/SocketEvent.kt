package com.doneit.ascend.domain.entity

enum class SocketEvent {
    PARTICIPANT_CONNECTED,
    PARTICIPANT_DISCONNECTED,
    TRACK_ADDED,
    RECORDING_STARTED,
    RECORDING_COMPLETED,
    ROOM_ENDED,
    RISE_A_HAND,
    REMOVE_HAND,
    UNEXPECTED;

    companion object {
        fun fromRemoteString(representation: String): SocketEvent {
            return when(representation) {
                "ParticipantConnected" -> PARTICIPANT_CONNECTED
                "ParticipantDisconnected" -> PARTICIPANT_DISCONNECTED
                "RoomEnded" -> ROOM_ENDED
                "TrackAdded" -> TRACK_ADDED
                "RecordingStarted" -> RECORDING_STARTED
                "RecordingCompleted" -> RECORDING_COMPLETED
                "RiseAHand" -> RISE_A_HAND
                "RemoveHand" -> REMOVE_HAND
                else -> UNEXPECTED
            }
        }
    }
}