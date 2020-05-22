package com.doneit.ascend.domain.entity.group

enum class GroupType {
    WEBINAR,
    SUPPORT,
    MASTER_MIND,
    INDIVIDUAL;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}