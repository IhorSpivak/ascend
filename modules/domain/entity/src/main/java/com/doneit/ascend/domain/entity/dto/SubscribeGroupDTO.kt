package com.doneit.ascend.domain.entity.dto

data class SubscribeGroupDTO(
    val groupId: Long,
    val paymentSourceId: Long?,
    val paymentSourceType: PaymentType?
)