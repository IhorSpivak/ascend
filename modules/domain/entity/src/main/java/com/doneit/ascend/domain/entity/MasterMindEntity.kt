package com.doneit.ascend.domain.entity

data class MasterMindEntity(
    val id: Long,
    val fullName: String?,
    val displayName: String?,
    val rating: Double,
    val followed: Boolean,
    val rated: Boolean,
    val image: ImageEntity?
)