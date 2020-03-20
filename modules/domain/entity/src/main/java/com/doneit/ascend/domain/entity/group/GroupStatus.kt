package com.doneit.ascend.domain.entity.group

enum class GroupStatus {
    UPCOMING,
    ACTIVE,
    STARTED,
    ENDED,
    CANCELLED;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}