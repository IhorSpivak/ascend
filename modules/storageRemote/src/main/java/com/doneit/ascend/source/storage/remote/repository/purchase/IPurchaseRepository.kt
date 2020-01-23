package com.doneit.ascend.source.storage.remote.repository.purchase

import com.doneit.ascend.source.storage.remote.data.request.PurchasesRequest
import com.doneit.ascend.source.storage.remote.data.response.PurchasesResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse

interface IPurchaseRepository {
    suspend fun getPurchases(request: PurchasesRequest): RemoteResponse<PurchasesResponse, ErrorsListResponse>
}