package com.doneit.ascend.domain.entity

import com.doneit.ascend.domain.entity.dto.GroupType

class GroupEntity(
    id: Long,
    val name: String?,
    val description: String?,
    val startTime: String?,
    val groupType: GroupType?,
    val price: Float?,
    val image: ImageEntity?,
    val createdAt: String?,
    val updatedAt: String?,
    val owner: OwnerEntity?,
    val participantsCount: Int?
): SearchEntity(id)