package com.doneit.ascend.domain.entity

import java.lang.IllegalArgumentException

enum class SocketEvent {
    PARTICIPANT_CONNECTED,
    PARTICIPANT_DISCONNECTED,
    ROOM_ENDED;

    companion object {
        fun fromRemoteString(representation: String): SocketEvent {
            return when(representation) {
                "ParticipantConnected" -> PARTICIPANT_CONNECTED
                "ParticipantDisconnected" -> PARTICIPANT_DISCONNECTED
                "RoomEnded" -> ROOM_ENDED
                else -> throw IllegalArgumentException()
            }
        }
    }
}