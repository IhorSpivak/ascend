package com.doneit.ascend.domain.entity.group

enum class GroupType {
    MASTER_MIND,
    WEBINARS,
    SUPPORT,
    INDIVIDUAL,
    //todo remove this locale types
    DAILY,
    MY_GROUPS;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}