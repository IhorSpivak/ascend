package com.doneit.ascend.domain.entity

import java.util.*

data class PurchaseEntity(
    val id: Long,
    val amount: Float,
    val groupId: Long,
    val groupName: String,
    val createdAt: Date,
    val paymentSource: PaymentSourceEntity
)