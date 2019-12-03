package com.doneit.ascend.presentation.utils

enum class Messages {
    DEFAULT_ERROR,
    PASSWORD_SENT,
    ACCOUNT_BLOCKED;

    fun getId(): Int {
        return ordinal
    }
}