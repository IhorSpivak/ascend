package com.doneit.ascend.presentation.main.create_group.common

enum class GroupType {
    WEBINAR,
    SUPPORT,
    MASTER_MIND
}

fun GroupType.toStringValue(): String {
    return when (this) {
        GroupType.WEBINAR -> "webinar"
        GroupType.SUPPORT -> ""
        GroupType.MASTER_MIND -> "master_mind"
    }
}