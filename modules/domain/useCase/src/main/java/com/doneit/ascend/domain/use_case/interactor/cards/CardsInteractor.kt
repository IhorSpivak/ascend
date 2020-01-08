package com.doneit.ascend.domain.use_case.interactor.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.doneit.ascend.domain.entity.CardEntity
import com.doneit.ascend.domain.use_case.gateway.ICardsGateway

class CardsInteractor(
    private val userGateway: ICardsGateway
) : CardsUseCase {
    override fun getAllCards(): LiveData<List<CardEntity>> {
        return liveData {
            emitSource(MutableLiveData())
            val result = userGateway.getAllCards()

            if (result.isSuccessful) {
                emit(result.successModel!!)
            }
        }
    }
}