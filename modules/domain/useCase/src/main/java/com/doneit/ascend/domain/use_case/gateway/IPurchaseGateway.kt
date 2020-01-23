package com.doneit.ascend.domain.use_case.gateway

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.PurchaseEntity
import com.doneit.ascend.domain.entity.dto.PurchasesModel

interface IPurchaseGateway {
    suspend fun getPurchases(model: PurchasesModel): PagedList<PurchaseEntity>
}