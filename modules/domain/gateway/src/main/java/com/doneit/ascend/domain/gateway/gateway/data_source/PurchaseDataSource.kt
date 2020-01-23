package com.doneit.ascend.domain.gateway.gateway.data_source

import androidx.paging.PageKeyedDataSource
import com.doneit.ascend.domain.entity.PurchaseEntity
import com.doneit.ascend.domain.entity.dto.PurchasesModel
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.source.storage.remote.repository.purchase.IPurchaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class PurchaseDataSource(
    private val scope: CoroutineScope,
    private val remote: IPurchaseRepository,
    private val requestModel: PurchasesModel
) : PageKeyedDataSource<Int, PurchaseEntity>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PurchaseEntity>
    ) {
        scope.launch {
            try {

                val page = 1

                val groups = remote.getPurchases(requestModel.toRequest(page)).toResponseEntity(
                    {
                        it?.purchases?.map { purchaseIt -> purchaseIt.toEntity() }
                    },
                    {
                        it?.errors
                    }
                )

                if (groups.isSuccessful) {
                    callback.onResult(groups.successModel ?: listOf(), null, page + 1)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PurchaseEntity>) {
        scope.launch {
            try {

                val page = params.key

                val groups =
                    remote.getPurchases(requestModel.toRequest(page)).toResponseEntity(
                        {
                            it?.purchases?.map { purchaseIt -> purchaseIt.toEntity() }
                        },
                        {
                            it?.errors
                        }
                    )

                if (groups.isSuccessful) {
                    callback.onResult(groups.successModel ?: listOf(), page + 1)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PurchaseEntity>) {
    }
}