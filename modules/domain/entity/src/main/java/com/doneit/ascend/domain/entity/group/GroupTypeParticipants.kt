package com.doneit.ascend.domain.entity.group

enum class GroupTypeParticipants {
    GROUP,
    INDIVIDUAL;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}