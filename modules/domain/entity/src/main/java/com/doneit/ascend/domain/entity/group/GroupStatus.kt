package com.doneit.ascend.domain.entity.group

enum class GroupStatus {
    UPCOMING,
    ACTIVE,
    STARTED,
    ENDED;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}