package com.doneit.ascend.source.storage.remote.repository.cards

import com.doneit.ascend.source.storage.remote.data.request.CreateCardRequest
import com.doneit.ascend.source.storage.remote.data.response.CardResponse
import com.doneit.ascend.source.storage.remote.data.response.CardsResponse
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse

interface ICardsRepository {
    suspend fun createCard(request: CreateCardRequest): RemoteResponse<CardResponse, ErrorsListResponse>

    suspend fun getAllCards(): RemoteResponse<CardsResponse, ErrorsListResponse>

    suspend fun deleteCard(id: Long): RemoteResponse<OKResponse, ErrorsListResponse>

    suspend fun asDefault(id: Long): RemoteResponse<OKResponse, ErrorsListResponse>
}