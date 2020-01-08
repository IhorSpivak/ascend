package com.doneit.ascend.domain.use_case.interactor.cards

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.CardEntity

interface CardsUseCase {
    fun getAllCards(): LiveData<List<CardEntity>>
}