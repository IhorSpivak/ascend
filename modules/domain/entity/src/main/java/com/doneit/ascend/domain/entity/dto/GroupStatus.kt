package com.doneit.ascend.domain.entity.dto

enum class GroupStatus {
    UPCOMING,
    ENDED;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}