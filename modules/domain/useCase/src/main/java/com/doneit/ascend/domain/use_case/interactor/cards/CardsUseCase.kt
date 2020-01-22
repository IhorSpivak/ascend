package com.doneit.ascend.domain.use_case.interactor.cards

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.CardEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.CreateCardModel

interface CardsUseCase {
    suspend fun createCard(model: CreateCardModel): ResponseEntity<CardEntity, List<String>>

    fun getAllCards(): LiveData<List<CardEntity>>

    suspend fun deleteCard(id: Long): ResponseEntity<Unit, List<String>>

    suspend fun asDefault(id: Long): ResponseEntity<Unit, List<String>>
}