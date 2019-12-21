package com.doneit.ascend.domain.entity.dto

enum class NotificationType {
    INVITE_TO_A_MEETING,
    NEW_GROUPS,
    MEETING_STARTED
}

fun String?.parseToNotificationType(): NotificationType {
    return when (this) {
        "meeting_started" -> NotificationType.MEETING_STARTED
        "new_groups" -> NotificationType.NEW_GROUPS
        "invite_to_a_meeting" -> NotificationType.INVITE_TO_A_MEETING
        else -> NotificationType.INVITE_TO_A_MEETING
    }
}