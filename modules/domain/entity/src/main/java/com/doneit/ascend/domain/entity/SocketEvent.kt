package com.doneit.ascend.domain.entity

enum class SocketEvent(
    val command: String
) {
    PARTICIPANT_CONNECTED("ParticipantConnected"),
    PARTICIPANT_DISCONNECTED("ParticipantDisconnected"),
    TRACK_ADDED("TrackAdded"),
    RECORDING_STARTED("RecordingStarted"),
    RECORDING_COMPLETED("RecordingCompleted"),
    ROOM_ENDED("RoomEnded"),
    RISE_A_HAND("RiseAHand"),
    REMOVE_HAND("RemoveHand"),
    REMOVED_FROM_GROUP("RemoveParticipant"),
    SPEAK("Speak"),
    GROUP_STARTED("StartGroup"),
    MUTE_USER("MuteUser"),
    RESET_MUTE_USER("ResetMuteUser"),
    UNEXPECTED("");

    override fun toString(): String {
        return command
    }

    companion object {
        fun fromRemoteString(representation: String): SocketEvent {
            return values().firstOrNull { it.command == representation } ?: UNEXPECTED
        }
    }
}