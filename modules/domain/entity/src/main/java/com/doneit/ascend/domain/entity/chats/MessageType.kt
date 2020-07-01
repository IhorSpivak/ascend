package com.doneit.ascend.domain.entity.chats

enum class MessageType {
    MESSAGE,
    INVITE,
    USER_REMOVED,
    LEAVE;
    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}