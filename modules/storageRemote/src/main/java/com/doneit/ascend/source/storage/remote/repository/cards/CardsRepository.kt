package com.doneit.ascend.source.storage.remote.repository.cards

import com.doneit.ascend.source.storage.remote.api.CardsApi
import com.doneit.ascend.source.storage.remote.data.response.CardsResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse
import com.doneit.ascend.source.storage.remote.repository.base.BaseRepository
import com.google.gson.Gson

internal class CardsRepository (
    gson: Gson,
    private val api: CardsApi
) : BaseRepository(gson), ICardsRepository {

    override suspend fun getAllCards(): RemoteResponse<CardsResponse, ErrorsListResponse> {
        return execute({ api.getAllCards() }, ErrorsListResponse::class.java)
    }
}