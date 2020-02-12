package com.doneit.ascend.domain.use_case.interactor.purchase

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.PurchaseEntity
import com.doneit.ascend.domain.entity.dto.PurchasesDTO
import com.doneit.ascend.domain.use_case.gateway.IPurchaseGateway

class PurchaseInteractor(
    private val gateway: IPurchaseGateway
) : PurchaseUseCase {
    override suspend fun getPurchases(model: PurchasesDTO): PagedList<PurchaseEntity> {
        return gateway.getPurchases(model)
    }
}