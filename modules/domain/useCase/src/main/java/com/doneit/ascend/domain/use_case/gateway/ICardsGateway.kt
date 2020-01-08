package com.doneit.ascend.domain.use_case.gateway

import com.doneit.ascend.domain.entity.CardEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity

interface ICardsGateway {
    suspend fun getAllCards(): ResponseEntity<List<CardEntity>, List<String>>
}