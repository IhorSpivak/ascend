package com.doneit.ascend.domain.gateway.gateway.data_source

import androidx.paging.PageKeyedDataSource
import com.doneit.ascend.domain.entity.RateEntity
import com.doneit.ascend.domain.entity.dto.RatingsModel
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.source.storage.remote.repository.user.IUserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class RateDataSource(
    private val scope: CoroutineScope,
    private val remote: IUserRepository,
    private val ratingsModel: RatingsModel
) : PageKeyedDataSource<Int, RateEntity>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, RateEntity>
    ) {
        scope.launch {
            try {

                val page = 1

                val groups = remote.getRates(ratingsModel.toRequest(page)).toResponseEntity(
                    {
                        it?.ratings?.map { it.toEntity() }
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

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, RateEntity>) {
        scope.launch {
            try {

                val page = params.key

                val groups =
                    remote.getRates(ratingsModel.toRequest(page)).toResponseEntity(
                        {
                            it?.ratings?.map { it.toEntity() }
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

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, RateEntity>) {
    }
}