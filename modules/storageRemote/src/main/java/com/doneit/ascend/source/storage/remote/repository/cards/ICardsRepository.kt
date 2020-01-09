package com.doneit.ascend.source.storage.remote.repository.cards

import com.doneit.ascend.source.storage.remote.data.response.CardsResponse
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse

interface ICardsRepository {
    suspend fun getAllCards(): RemoteResponse<CardsResponse, ErrorsListResponse>

    suspend fun deleteCard(id: Long): RemoteResponse<OKResponse, ErrorsListResponse>
}