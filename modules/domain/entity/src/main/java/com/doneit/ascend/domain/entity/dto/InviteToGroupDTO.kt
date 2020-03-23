package com.doneit.ascend.domain.entity.dto

data class InviteToGroupDTO(
    val groupId: Long,
    val participants: List<String>
)