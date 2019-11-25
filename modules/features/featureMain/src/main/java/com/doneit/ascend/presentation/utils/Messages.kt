package com.doneit.ascend.presentation.utils

enum class Messages {
    EROR,
    PASSWORD_SENT,
    ACCOUNT_BLOCKED;

    fun getId(): Int {
        return ordinal
    }
}