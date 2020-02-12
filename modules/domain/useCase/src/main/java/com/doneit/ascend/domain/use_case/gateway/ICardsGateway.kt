package com.doneit.ascend.domain.use_case.gateway

import com.doneit.ascend.domain.entity.CardEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.CreateCardDTO

interface ICardsGateway {
    suspend fun createCard(model: CreateCardDTO): ResponseEntity<CardEntity, List<String>>

    suspend fun getAllCards(): ResponseEntity<List<CardEntity>, List<String>>

    suspend fun deleteCard(id: Long): ResponseEntity<Unit, List<String>>

    suspend fun asDefault(id: Long): ResponseEntity<Unit, List<String>>
}