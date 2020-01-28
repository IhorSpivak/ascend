package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.PaymentSourceEntity
import com.doneit.ascend.domain.entity.PaymentSourceType
import com.doneit.ascend.domain.entity.PurchaseEntity
import com.doneit.ascend.source.storage.remote.data.response.PaymentSourceResponse
import com.doneit.ascend.source.storage.remote.data.response.PurchaseResponse

fun PurchaseResponse.toEntity(): PurchaseEntity {
    return PurchaseEntity(
        id,
        amount / 100,
        groupId,
        groupName,
        createdAt.toDate()!!,
        paymentSource.toEntity()
    )
}

fun PaymentSourceResponse.toEntity(): PaymentSourceEntity {
    return PaymentSourceEntity(
        sourceId,
        sourceType.toSourceType(),
        name,
        brand?.toCardBrend(),
        last4
    )
}

private fun String.toSourceType(): PaymentSourceType {
    return when(this) {
        "Card" -> PaymentSourceType.CARD
        else -> PaymentSourceType.CARD
    }
}