package com.doneit.ascend.domain.entity.dto

enum class GroupType {
    MASTER_MIND,
    WEBINAR,
    RECOVERY,
    FAMILY,
    SUCCESS,
    SPIRITUAL,
    SUPPORT,
    DAILY;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}

fun GroupType.toStringValue(): String {
    return when (this) {
        GroupType.MASTER_MIND -> "master_mind"
        GroupType.WEBINAR -> "webinar"
        GroupType.RECOVERY -> "recovery"
        GroupType.FAMILY -> "family"
        GroupType.SUCCESS -> "success"
        GroupType.SPIRITUAL -> "spiritual"
        GroupType.SUPPORT -> "support"
        GroupType.DAILY -> "daily"
    }
}

fun GroupType.toStringValueUI(): String {
    return when (this) {
        GroupType.MASTER_MIND -> "Master Mind"
        GroupType.WEBINAR -> "Webinar"
        GroupType.RECOVERY -> "Recovery"
        GroupType.FAMILY -> "Family"
        GroupType.SUCCESS -> "Success"
        GroupType.SPIRITUAL -> "Spiritual"
        GroupType.SUPPORT -> "Support"
        GroupType.DAILY -> "Daily"
    }
}