package com.doneit.ascend.domain.entity.dto

enum class ChatType(val type: String) {
    CHAT("chat"),
    CHANNEL("channel");

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}