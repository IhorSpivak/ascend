package com.doneit.ascend.domain.use_case.interactor.cards

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.CardEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity

interface CardsUseCase {
    fun getAllCards(): LiveData<List<CardEntity>>

    suspend fun deleteCard(id: Long): ResponseEntity<Unit, List<String>>
}