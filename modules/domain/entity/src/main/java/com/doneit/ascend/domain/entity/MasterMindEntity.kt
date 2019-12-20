package com.doneit.ascend.domain.entity

class MasterMindEntity(
    id: Long,
    val fullName: String?,
    val displayName: String?,
    val description: String?,
    val location: String?,
    val bio: String?,
    val groupsCount: Int?,
    val rating: Float,
    val followed: Boolean,
    val rated: Boolean,
    val image: ImageEntity?,
    val allowRating: Boolean?
): SearchEntity(id)