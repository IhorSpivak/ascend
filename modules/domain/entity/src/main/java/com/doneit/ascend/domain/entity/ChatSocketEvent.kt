package com.doneit.ascend.domain.entity

enum class ChatSocketEvent(
    val command: String
) {
    MESSAGE(""),
    DESTROY("destroy"),
    READ("read");

    override fun toString(): String {
        return command
    }

    companion object {
        fun fromRemoteString(representation: String): ChatSocketEvent {
            return values()
                .firstOrNull { it.command == representation } ?: MESSAGE
        }
    }
}