package com.doneit.ascend.source.storage.remote.repository.cards

import com.doneit.ascend.source.storage.remote.api.CardsApi
import com.doneit.ascend.source.storage.remote.data.request.CreateCardRequest
import com.doneit.ascend.source.storage.remote.data.response.CardResponse
import com.doneit.ascend.source.storage.remote.data.response.CardsResponse
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse
import com.doneit.ascend.source.storage.remote.repository.base.BaseRepository
import com.google.gson.Gson

internal class CardsRepository(
    gson: Gson,
    private val api: CardsApi
) : BaseRepository(gson), ICardsRepository {

    override suspend fun createCard(request: CreateCardRequest): RemoteResponse<CardResponse, ErrorsListResponse> {
        return execute({ api.createCard(request) }, ErrorsListResponse::class.java)
    }

    override suspend fun getAllCards(): RemoteResponse<CardsResponse, ErrorsListResponse> {
        return execute({ api.getAllCardsAsync() }, ErrorsListResponse::class.java)
    }

    override suspend fun deleteCard(id: Long): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.deleteCardAsync(id) }, ErrorsListResponse::class.java)
    }
}