package com.doneit.ascend.domain.entity

data class PaymentSourceEntity(
    val sourceId: Long,
    val sourceType: PaymentSourceType,
    val name: String?,
    val brand: CardType?,
    val last4: String?
)