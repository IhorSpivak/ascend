package com.doneit.ascend.domain.entity.dto

data class SubscribeGroupModel(
    val groupId: Long,
    val paymentSourceId: Long,
    val paymentSourceType: PaymentType
)