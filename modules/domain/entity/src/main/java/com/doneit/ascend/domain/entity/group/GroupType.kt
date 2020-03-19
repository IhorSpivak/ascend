package com.doneit.ascend.domain.entity.group

enum class GroupType {
    WEBINARS,
    SUPPORT,
    MASTER_MIND,
    INDIVIDUAL,
    //todo remove this locale types
    DAILY,
    MY_GROUPS;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}