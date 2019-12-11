package com.doneit.ascend.domain.entity

data class GroupEntity(
    val id: Long,
    val name: String?,
    val description: String?,
    val startTime: String?,
    val groupType: String?,
    val price: Long?,
    val image: ImageEntity?,
    val createdAt: String?,
    val updatedAt: String?,
    val owner: OwnerEntity?,
    val participantsCount: Int?
)