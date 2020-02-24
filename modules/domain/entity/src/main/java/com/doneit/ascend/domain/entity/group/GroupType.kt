package com.doneit.ascend.domain.entity.group

enum class GroupType {
    MASTER_MIND,
    WEBINARS,
    SUPPORT,
    //todo remove this locale types
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
        "support" -> GroupType.SUPPORT
        "daily" -> GroupType.DAILY
        else -> GroupType.MY_GROUPS
    }
}