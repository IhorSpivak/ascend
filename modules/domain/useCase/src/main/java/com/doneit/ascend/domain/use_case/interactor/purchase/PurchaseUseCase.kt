package com.doneit.ascend.domain.use_case.interactor.purchase

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.PurchaseEntity
import com.doneit.ascend.domain.entity.dto.PurchasesDTO

interface PurchaseUseCase {
    suspend fun getPurchases(model: PurchasesDTO): PagedList<PurchaseEntity>
}