package com.doneit.ascend.domain.gateway.gateway

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.PurchaseEntity
import com.doneit.ascend.domain.entity.dto.PurchasesModel
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.gateway.gateway.data_source.PurchaseDataSource
import com.doneit.ascend.domain.use_case.gateway.IPurchaseGateway
import com.doneit.ascend.source.storage.remote.repository.purchase.IPurchaseRepository
import com.vrgsoft.networkmanager.NetworkManager
import kotlinx.coroutines.GlobalScope

class PurchasesGateway(
    errors: NetworkManager,
    private val remote: IPurchaseRepository
) : BaseGateway(errors), IPurchaseGateway {
    override fun <T> calculateMessage(error: T): String {
        return ""
    }

    override suspend fun getPurchases(model: PurchasesModel): PagedList<PurchaseEntity> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(model.perPage ?: 10)
            .build()

        val dataSource = PurchaseDataSource(
            GlobalScope,
            remote,
            model
        )
        val executor = MainThreadExecutor()

        return PagedList.Builder<Int, PurchaseEntity>(dataSource, config)
            .setFetchExecutor(executor)
            .setNotifyExecutor(executor)
            .build()
    }
}