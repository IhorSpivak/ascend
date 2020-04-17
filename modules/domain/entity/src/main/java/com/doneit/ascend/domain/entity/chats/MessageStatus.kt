package com.doneit.ascend.domain.entity.chats

enum class MessageStatus {
    SENT, DELIVERED, READ, ALL;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}
