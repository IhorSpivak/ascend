package com.doneit.ascend.domain.use_case.interactor.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.doneit.ascend.domain.entity.CardEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.CreateCardModel
import com.doneit.ascend.domain.use_case.gateway.ICardsGateway

class CardsInteractor(
    private val cardsGateway: ICardsGateway
) : CardsUseCase {
    override suspend fun createCard(model: CreateCardModel): ResponseEntity<CardEntity, List<String>> {
        return cardsGateway.createCard(model)
    }

    override fun getAllCards(): LiveData<List<CardEntity>> {
        return liveData {
            emitSource(MutableLiveData())
            val result = cardsGateway.getAllCards()

            if (result.isSuccessful) {
                emit(result.successModel!!)
            }
        }
    }

    override suspend fun deleteCard(id: Long): ResponseEntity<Unit, List<String>> {
        return cardsGateway.deleteCard(id)
    }

    override suspend fun asDefault(id: Long): ResponseEntity<Unit, List<String>> {
        return cardsGateway.asDefault(id)
    }
}