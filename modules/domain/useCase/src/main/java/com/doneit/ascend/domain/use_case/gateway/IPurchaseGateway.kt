package com.doneit.ascend.domain.use_case.gateway

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.PurchaseEntity
import com.doneit.ascend.domain.entity.dto.PurchasesDTO

interface IPurchaseGateway {
    suspend fun getPurchases(model: PurchasesDTO): PagedList<PurchaseEntity>
}