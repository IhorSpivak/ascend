package com.doneit.ascend.domain.entity.chats

enum class MessageType {
    MESSAGE,
    INVITE,
    USER_REMOVED,
    LEAVE,
    POST_SHARE,
    ATTACHMENT,
    PROFILE_SHARE,
    GROUP_SHARE;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}