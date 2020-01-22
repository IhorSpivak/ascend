package com.doneit.ascend.domain.gateway.gateway

import com.doneit.ascend.domain.entity.CardEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.CreateCardModel
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
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

    override suspend fun createCard(model: CreateCardModel): ResponseEntity<CardEntity, List<String>> {
        return remote.createCard(model.toRequest()).toResponseEntity(
            {
                it?.toEntity()
            },
            {
                it?.errors
            }
        )
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

    override suspend fun deleteCard(id: Long): ResponseEntity<Unit, List<String>> {
        return remote.deleteCard(id).toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun asDefault(id: Long): ResponseEntity<Unit, List<String>> {
        return remote.asDefault(id).toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }
}