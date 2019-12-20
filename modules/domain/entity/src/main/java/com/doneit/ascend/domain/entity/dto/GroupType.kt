package com.doneit.ascend.domain.entity.dto

enum class GroupType {
    MASTER_MIND,
    WEBINARS,
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
        GroupType.WEBINARS -> "webinar"
        GroupType.RECOVERY -> "recovery"
        GroupType.FAMILY -> "family"
        GroupType.SUCCESS -> "success"
        GroupType.SPIRITUAL -> "spiritual"
        GroupType.SUPPORT -> "support"
        GroupType.DAILY -> "daily"
    }
}

fun String?.parseTo(): GroupType {
    return when (this?.toLowerCase()) {
        "master_mind" -> GroupType.MASTER_MIND
        "webinar" -> GroupType.WEBINARS
        "recovery" -> GroupType.RECOVERY
        "family" -> GroupType.FAMILY
        "success" -> GroupType.SUCCESS
        "spiritual" -> GroupType.SPIRITUAL
        "support" -> GroupType.SUPPORT
        "daily" -> GroupType.DAILY
        else -> GroupType.MASTER_MIND
    }
}

fun GroupType.toStringValueUI(): String {
    return when (this) {
        GroupType.MASTER_MIND -> "Master Mind"
        GroupType.WEBINARS -> "Webinars"
        GroupType.RECOVERY -> "Recovery"
        GroupType.FAMILY -> "Family"
        GroupType.SUCCESS -> "Success"
        GroupType.SPIRITUAL -> "Spiritual"
        GroupType.SUPPORT -> "Support"
        GroupType.DAILY -> "Daily"
    }
}