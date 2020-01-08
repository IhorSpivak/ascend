package com.doneit.ascend.presentation.models

import com.doneit.ascend.domain.entity.CardType
import java.util.*

data class PresentationCardModel(
    var id: Long,
    var name: String,
    var brand: CardType,
    var country: String,
    var expMonth: Int,
    var expYear: Int,
    var last4: String,
    var cratedAt: Date,
    var updatedAt: Date?,
    var isSelected: Boolean
)