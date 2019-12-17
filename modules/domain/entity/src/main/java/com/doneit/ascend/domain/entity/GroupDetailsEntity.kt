package com.doneit.ascend.domain.entity

import com.doneit.ascend.domain.entity.dto.GroupType

data class GroupDetailsEntity (
    val id: Long,
    val name: String?,
    val description: String?,
    val startTime: String?,
    val groupType: GroupType?,
    val price: Float?,
    val image: ImageEntity?,
    val meetingsCount: Int?,
    val createdAt: String?,
    val updatedAt: String?,
    val owner: OwnerEntity?,
    val subscribed: Boolean?,
    val invited: Boolean?,
    val participantsCount: Int?,
    val invitesCount: Int?
)