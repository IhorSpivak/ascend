package com.doneit.ascend.domain.entity.dto

enum class GroupType {
    MASTER_MIND,
    WEBINARS,
    RECOVERY,
    FAMILY,
    SUCCESS,
    SPIRITUAL,
    SUPPORT,
    DAILY,
    MY_GROUPS;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}

//todo replace with @SerializedName
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
        else -> GroupType.MY_GROUPS
    }
}

//todo refactor
fun GroupType.toStringValueUI(): String {
    return when (this) {
        GroupType.MASTER_MIND -> "MasterMind"
        GroupType.WEBINARS -> "Webinars"
        GroupType.RECOVERY -> "Recovery"
        GroupType.FAMILY -> "Family"
        GroupType.SUCCESS -> "Success"
        GroupType.SPIRITUAL -> "Spiritual"
        GroupType.SUPPORT -> "Support"
        GroupType.DAILY -> "My Daily"
        GroupType.MY_GROUPS -> "My"
    }
}