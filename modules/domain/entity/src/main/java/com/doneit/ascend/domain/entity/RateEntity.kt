package com.doneit.ascend.domain.entity

import java.util.*

data class RateEntity(
    val value: Float,
    val userId: Long,
    val fullName: String?,
    val image: ImageEntity,
    val createdAt: Date,
    val updatedAt: Date?
)