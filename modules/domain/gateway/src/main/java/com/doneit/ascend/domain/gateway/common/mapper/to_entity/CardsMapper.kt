package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.CardEntity
import com.doneit.ascend.domain.entity.CardType
import com.doneit.ascend.source.storage.remote.data.response.CardResponse

fun CardResponse.toEntity(): CardEntity {
    return CardEntity(
        id,
        name,
        brand.toCardType(),
        country,
        expMonth,
        expYear,
        last4,
        cratedAt.toDate()!!,
        updatedAt.toDate(),
        isDefault
    )
}

fun String.toCardType(): CardType {
    return when(this) {
        "Visa" -> CardType.VISA
        else -> CardType.MASTER_CARD
    }
}