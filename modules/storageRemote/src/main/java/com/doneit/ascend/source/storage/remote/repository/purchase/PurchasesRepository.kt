package com.doneit.ascend.source.storage.remote.repository.purchase

import com.doneit.ascend.source.storage.remote.api.PurchaseApi
import com.doneit.ascend.source.storage.remote.data.request.PurchasesRequest
import com.doneit.ascend.source.storage.remote.data.response.PurchasesResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse
import com.doneit.ascend.source.storage.remote.repository.base.BaseRepository
import com.google.gson.Gson

internal class PurchasesRepository(
    gson: Gson,
    private val api: PurchaseApi
) : BaseRepository(gson), IPurchaseRepository {
    override suspend fun getPurchases(request: PurchasesRequest): RemoteResponse<PurchasesResponse, ErrorsListResponse> {
        return execute({
            api.getPurchasesList(
                request.page,
                request.perPage,
                request.sortColumn,
                request.sortType,
                request.createdAtFrom,
                request.createdAtTo
            )
        }, ErrorsListResponse::class.java)
    }
}