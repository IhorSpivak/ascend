package com.doneit.ascend.domain.entity.chats

enum class MessageType {
    MESSAGE,
    INVITE,
    LEAVE;
    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}