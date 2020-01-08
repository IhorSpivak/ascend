package com.doneit.ascend.domain.entity

import java.util.*

data class CardEntity(
    val id: Long,
    val name: String,
    val brand: CardType,
    val country: String,
    val expMonth: Int,
    val expYear: Int,
    val last4: String,
    val createdAt: Date,
    val updatedAt: Date?,
    val isDefault: Boolean
)