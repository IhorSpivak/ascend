package com.doneit.ascend.domain.entity.notification

enum class NotificationType {
    INVITE_TO_A_MEETING,
    NEW_GROUPS,
    MEETING_STARTED,
    REMOVED_FROM_GROUP,
    TEN_MINUTES_TO_START,
    UNEXPECTED;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }

    companion object {
        fun fromString(representation: String): NotificationType {
            return when (representation) {
                "invite_to_a_meeting" -> INVITE_TO_A_MEETING
                "new_groups" -> NEW_GROUPS
                "meeting_started" -> MEETING_STARTED
                "removed_from_group" -> REMOVED_FROM_GROUP
                "ten_minutes_to_start_meeting" -> TEN_MINUTES_TO_START
                else -> UNEXPECTED
            }
        }
    }
}