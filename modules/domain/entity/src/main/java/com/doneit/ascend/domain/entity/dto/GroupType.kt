package com.doneit.ascend.domain.entity.dto

enum class GroupType {
    MASTER_MIND,
    WEBINAR,
    RECOVERY,
    FAMILY,
    SUCCESS,
    SPIRITUAL;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}