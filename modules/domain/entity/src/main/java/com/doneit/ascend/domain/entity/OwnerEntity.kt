package com.doneit.ascend.domain.entity

data class OwnerEntity(
    val id: Long,
    val fullName: String,
    val image: ImageEntity,
    val rating: Float,
    val followed: Boolean
)