package com.doneit.ascend.domain.entity

import com.doneit.ascend.domain.entity.dto.GroupType
import java.util.*

data class GroupDetailsEntity (
    val id: Long,
    val name: String?,
    val description: String?,
    val startTime: Date?,
    val groupType: GroupType?,
    val price: Float?,
    val image: ImageEntity?,
    val meetingsCount: Int?,
    val createdAt: Date?,
    val updatedAt: Date?,
    val owner: OwnerEntity?,
    val subscribed: Boolean?,
    val invited: Boolean?,
    val participantsCount: Int?,
    val invitesCount: Int?
)