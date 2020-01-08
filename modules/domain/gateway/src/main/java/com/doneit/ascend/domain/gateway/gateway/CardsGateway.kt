package com.doneit.ascend.domain.gateway.gateway

import com.doneit.ascend.domain.entity.CardEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.use_case.gateway.ICardsGateway
import com.doneit.ascend.source.storage.remote.repository.cards.ICardsRepository
import com.vrgsoft.networkmanager.NetworkManager

class CardsGateway (
    errors: NetworkManager,
    private val remote: ICardsRepository
) : BaseGateway(errors), ICardsGateway {
    override fun <T> calculateMessage(error: T): String {
        return  ""//todo, not required for now
    }

    override suspend fun getAllCards(): ResponseEntity<List<CardEntity>, List<String>> {
        return remote.getAllCards().toResponseEntity(
            {
                it?.cards?.map { it.toEntity() }
            },
            {
                it?.errors
            }
        )
    }
}