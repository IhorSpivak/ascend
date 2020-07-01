package com.doneit.ascend.presentation.main.chats.chat.common

enum class ChatType(val type: String) {
    WEBINAR_CHAT("webinarChat"),
    CHAT("chat");

    override fun toString(): String {
        return type
    }

    companion object {
        fun fromRemoteString(type: String): ChatType {
            return values().firstOrNull { it.type == type } ?: CHAT
        }
    }
}